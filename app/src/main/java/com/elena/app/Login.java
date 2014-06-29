package com.elena.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by elena on 15/06/2014.
 */
public class Login extends ActionBarActivity implements View.OnClickListener {

    private ProgressDialog _progressDialog;
    private boolean _progressDialogRunning = false;
    private CheckoutButton launchPayPalButton;
    final static public int PAYPAL_BUTTON_ID = 10001;
    private static final int REQUEST_PAYPAL_CHECKOUT = 2;
    Event e;
    Gnammo g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        g=(Gnammo)this.getApplication();
        if ( g.getLog() == 0 ) {
            setContentView(R.layout.login);
        } else {
            load_already_logged();
        }
        e=(Event)getIntent().getSerializableExtra("event");


    }

    private void load_already_logged() {
        setContentView(R.layout.already_logged);
        // insert the PayPal button
        // Check if the PayPal Library has been initialized yet. If it has, show
        // the "Pay with PayPal button"
        // If not, show a progress indicator and start a loop that keeps
        // checking the init status
        if (g.getpaypalLibraryInit()) {
            showPayPalButton();
        } else {
            // Display a progress dialog to the user and start checking for when
            // the initialization is completed
            _progressDialog = new ProgressDialog(this);
            _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            _progressDialog.setMessage("Loading PayPal Payment Library");
            _progressDialog.setCancelable(false);
            _progressDialog.show();
            _progressDialogRunning = true;
            Thread newThread = new Thread(checkforPayPalInitRunnable);
            newThread.start();
        }

    }

    public void onClick(View arg0) {

        if (arg0 == (Button) findViewById(R.id.button)) {
            POST("http://gnammo.com/api/3/accounts/login");
        }
        else if (arg0 == (CheckoutButton) findViewById(PAYPAL_BUTTON_ID)) {
            PayPalButtonClick(arg0);
        }

    }

    public void POST(String url) {
        InputStream inputStream = null;
        EditText e = (EditText) findViewById(R.id.editText);
        String name = e.getText().toString();
        EditText e1 = (EditText) findViewById(R.id.editText2);
        String psw = e1.getText().toString();
        String result = "";

        try {
            // 1. create HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", name);
            jsonObject.accumulate("password", psw);
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            LoginJson l = gson.fromJson(reader, LoginJson.class);

            if (l.getStatus() == 200) {
                g.setLOg(1);
                g.setUser(name);
                TextView t5 = (TextView) findViewById(R.id.textView4);
                t5.setText("Benvenuto "+g.getUser() );
                // insert the PayPal button
                // Check if the PayPal Library has been initialized yet. If it has, show
                // the "Pay with PayPal button"
                // If not, show a progress indicator and start a loop that keeps
                // checking the init status

                if (g.getpaypalLibraryInit()) {
                    showPayPalButton();
                } else {
                    // Display a progress dialog to the user and start checking for when
                    // the initialization is completed
                    _progressDialog = new ProgressDialog(this);
                    _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    _progressDialog.setMessage("Loading PayPal Payment Library");
                    _progressDialog.setCancelable(false);
                    _progressDialog.show();
                    _progressDialogRunning = true;
                    Thread newThread = new Thread(checkforPayPalInitRunnable);
                    newThread.start();
                }
            } else {
                TextView t5 = (TextView) findViewById(R.id.textView4);
                t5.setText("Login Fallito" );
            }

        } catch (Exception err) {
            result = "InputStream" + err.getMessage();
        }
    }

    // PayPal Activity Results. This handles all the responses from the PayPal
    // Payments Library
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQUEST_PAYPAL_CHECKOUT) {
            PayPalActivityResult(requestCode, resultCode, intent);
        }

    }


    /**********************************
     * PayPal library related methods
     **********************************/


    // This lets us show the PayPal Button after the library has been
    // initialized
    final Runnable showPayPalButtonRunnable = new Runnable() {
        public void run() {
            showPayPalButton();
        }
    };

    // This lets us run a loop to check the status of the PayPal Library init
    final Runnable checkforPayPalInitRunnable = new Runnable() {
        public void run() {
            checkForPayPalLibraryInit();
        }
    };

    // This method is called if the Review page is being loaded but the PayPal
    // Library is not
    // initialized yet.
    private void checkForPayPalLibraryInit() {
        // Loop as long as the library is not initialized

        while (g.getpaypalLibraryInit() == false) {
            try {
                // wait 1/2 a second then check again
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Show an error to the user
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Error initializing PayPal Library")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // Could do anything here to handle the
                                        // error
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        // If we got here, it means the library is initialized.
        // So, add the "Pay with PayPal" button to the screen
        runOnUiThread(showPayPalButtonRunnable);
    }


//	  The initLibrary function takes care of all the basic Library
//	  initialization.
//
//	  @return The return will be true if the initialization was successful and
//	          false if

    public void initLibrary() {
        PayPal pp = PayPal.getInstance();
        // If the library is already initialized, then we don't need to
        // initialize it again.
        if (pp == null) {
            // This is the main initialization call that takes in your Context,
            // the Application ID, and the server you would like to connect to.
            pp =  PayPal.initWithAppID(this, "APP-80W284485P519543T",
                  PayPal.ENV_SANDBOX);
            // -- These are required settings.
            pp.setLanguage("en_US"); // Sets the language for the library.
            // --

            // -- These are a few of the optional settings.
            // Sets the fees payer. If there are fees for the transaction, this
            // person will pay for them. Possible values are FEEPAYER_SENDER,
            // FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and
            // FEEPAYER_SECONDARYONLY.
            pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
            // Set to true if the transaction will require shipping.
            pp.setShippingEnabled(true);
            // Dynamic Amount Calculation allows you to set tax and shipping
            // amounts based on the user's shipping address. Shipping must be
            // enabled for Dynamic Amount Calculation. This also requires you to
            // create a class that implements PaymentAdjuster and Serializable.
            pp.setDynamicAmountCalculationEnabled(false);
            // --
            g.setpaypalLibraryInit(true);
        }
    }

    //	   this method generates the PayPal checkout button and adds it to the current view
//	   using the relative layout params
    private void showPayPalButton() {
        removePayPalButton();
        // Back in the UI thread -- show the "Pay with PayPal" button
        // Generate the PayPal Checkout button and save it for later use
        PayPal pp = PayPal.getInstance();
        launchPayPalButton = pp.getCheckoutButton(this, PayPal.BUTTON_278x43,
                CheckoutButton.TEXT_PAY);
        // You'll need to have an OnClickListener for the CheckoutButton.
        launchPayPalButton.setOnClickListener(this);
        // add it to the layout
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 10;

        launchPayPalButton.setLayoutParams(params);
        launchPayPalButton.setId(PAYPAL_BUTTON_ID);
        ((LinearLayout) findViewById(R.id.login))
                .addView(launchPayPalButton);
        ((LinearLayout) findViewById(R.id.login))
                .setGravity(Gravity.CENTER_HORIZONTAL);
        if (_progressDialogRunning) {
            _progressDialog.dismiss();
            _progressDialogRunning = false;
        }
    }

    //	 this method removes the PayPal button from the view
    private void removePayPalButton() {
        // Avoid an exception for setting a parent more than once
        if (launchPayPalButton != null) {
            ((LinearLayout) findViewById(R.id.login))
                    .removeView(launchPayPalButton);
        }
    }

    //	method to handle PayPal checkout button onClick event
//	- this must be called from the onClick() method implemented by the application
//
    public void PayPalButtonClick(View arg0) {
        // Create a basic PayPalPayment.
        PayPalPayment payment = new PayPalPayment();
        // Sets the currency type for this payment.
        payment.setCurrencyType("EUR");
        // Sets the recipient for the payment. This can also be a phone
        // number.
        payment.setRecipient("email@email.com");
        // Sets the amount of the payment, not including tax and shipping
        // amounts.
        BigDecimal st = new BigDecimal(getIntent().getExtras().getDouble("price"));
        st = st.setScale(2, RoundingMode.HALF_UP);
        payment.setSubtotal(st);
        // Sets the payment type. This can be PAYMENT_TYPE_GOODS,
        // PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or
        // PAYMENT_TYPE_NONE.
        payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);

        // PayPalInvoiceData can contain tax and shipping amounts. It also
        // contains an ArrayList of PayPalInvoiceItem which can
        // be filled out. These are not required for any transaction.
        PayPalInvoiceData invoice = new PayPalInvoiceData();
        // Sets the tax amount.
        //BigDecimal tax = new BigDecimal("3");
        //tax = tax.setScale(2, RoundingMode.HALF_UP);
        //invoice.setTax(tax);




        // PayPalInvoiceItem has several parameters available to it. None of these parameters is required.
        PayPalInvoiceItem item1 = new PayPalInvoiceItem();
        // Sets the name of the item.
        item1.setName(getIntent().getExtras().getString("title"));
        // Sets the ID. This is any ID that you would like to have associated with the item.
        //item1.setID("1234");
        // Sets the total price which should be (quantity * unit price). The total prices of all PayPalInvoiceItem should add up
        // to less than or equal the subtotal of the payment.
        item1.setTotalPrice(new BigDecimal(getIntent().getExtras().getDouble("price")));
        // Sets the unit price.
        item1.setUnitPrice(new BigDecimal(getIntent().getExtras().getDouble("price")));
        // Sets the quantity.
        item1.setQuantity(1);
        // Add the PayPalInvoiceItem to the PayPalInvoiceData. Alternatively, you can create an ArrayList<PayPalInvoiceItem>
        // and pass it to the PayPalInvoiceData function setInvoiceItems().
        invoice.getInvoiceItems().add(item1);
        // add toppings as extra items


        // Sets the PayPalPayment invoice data.
        payment.setInvoiceData(invoice);
        // Sets the merchant name. This is the name of your Application or
        // Company.
        payment.setMerchantName("Gnammo");
        // Sets the description of the payment.
        payment.setDescription(getIntent().getExtras().getString("event_description"));

        // Use checkout to create our Intent.
        Intent checkoutIntent = PayPal.getInstance()
                .checkout(payment, this);
        //Intent checkoutIntent = PayPal.getInstance()
        //		.checkout(payment, this , new ResultDelegate());

        // Use the android's startActivityForResult() and pass in our
        // Intent.
        // This will start the library.
        startActivityForResult(checkoutIntent, REQUEST_PAYPAL_CHECKOUT);
    }

//	 This method handles the PayPal Activity Results. This handles all the responses from the PayPal
//	 Payments Library.
//	 This method must be called from the application's onActivityResult() handler

    public void PayPalActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                // The payment succeeded
                String payKey = intent
                        .getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                this.paymentSucceeded(payKey);
                break;
            case Activity.RESULT_CANCELED:
                // The payment was canceled
                this.paymentCanceled();
                break;
            case PayPalActivity.RESULT_FAILURE:
                // The payment failed -- we get the error from the
                // EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
                String errorID = intent
                        .getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                String errorMessage = intent
                        .getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                this.paymentFailed(errorID, errorMessage);
        }
    }


    //text

    public void paymentSucceeded(String payKey) {
        // We could show the transactionID to the user

    }

    public void paymentFailed(String errorID, String errorMessage) {
        // We could let the user know the payment failed here

    }

    public void paymentCanceled() {

    }


}




