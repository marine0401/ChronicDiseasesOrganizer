package com.example.miqyasapp1;

import androidx.annotation.StringRes;

public class DialogFactory {

    public DialogFactory() {
    }

    public static OneButtonDialog makeSuccessDialog(@StringRes int titleId,
                                             @StringRes int messageId,
                                             @StringRes int buttonTextId,
                                             OneButtonDialog.ButtonDialogAction action) {
        return OneButtonDialog.newInstance(titleId,
                messageId,
                buttonTextId,
                R.drawable.icon_check,
                R.color.green,
                action);
    }
//
//    static OneButtonDialog makeErrorDialog(@StringRes int titleId,
//                                           @StringRes int messageId,
//                                           @StringRes int buttonTextId,
//                                           OneButtonDialog.ButtonDialogAction action) {
//        return OneButtonDialog.newInstance(titleId,
//                messageId,
//                buttonTextId,
//                R.drawable.ic_close,
//                R.color.red_500,
//                action);
//    }

}