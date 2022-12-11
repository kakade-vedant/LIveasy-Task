package com.vedantkakade.liveasytask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SMSBroadCastReceiver extends BroadcastReceiver {

    public SmsBroadCaseReceiverListener smsBroadCaseReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == (SmsRetriever.SMS_RETRIEVED_ACTION)) {
            Bundle extras = intent.getExtras();

            Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (smsRetrieverStatus.getStatusCode()) {
                case CommonStatusCodes
                        .SUCCESS:
                    Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    smsBroadCaseReceiverListener.onSuccess(messageIntent);
                    break;

                case CommonStatusCodes.TIMEOUT:
                    smsBroadCaseReceiverListener.onFailure();
                    break;
            }
        }
    }

    public interface SmsBroadCaseReceiverListener {
        void onSuccess(Intent intent);
        void onFailure();
    }
}
