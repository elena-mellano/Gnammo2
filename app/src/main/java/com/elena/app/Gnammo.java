package com.elena.app;

import android.app.Application;

import com.paypal.android.MEP.PayPal;

/**
 * Created by elena on 26/06/2014.
 */
public class Gnammo  extends Application {

    private String user;
    private int log = 0;
    private boolean _paypalLibraryInit=false;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getLog() {
        return log;
    }

    public void setLOg(int i) {
        this.log = i;
    }

    public boolean getpaypalLibraryInit(){return _paypalLibraryInit;}
    public void setpaypalLibraryInit(boolean b){_paypalLibraryInit=b;}

    public void onCreate() {
        Thread libraryInitializationThread = new Thread() {
            public void run() {
                initLibrary();
            }
        };
        libraryInitializationThread.start();

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
            pp = PayPal.initWithAppID(this, "APP-80W284485P519543T",
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
            _paypalLibraryInit = true;
        }
    }
}