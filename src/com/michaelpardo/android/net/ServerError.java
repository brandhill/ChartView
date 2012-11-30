package com.michaelpardo.android.net;

import org.apache.http.StatusLine;

public class ServerError {
        private String mMessage;

        public ServerError() {
        }

        public ServerError(StatusLine statusLine) {
                mMessage = statusLine.getReasonPhrase();
        }

        public String getMessage() {
                return mMessage;
        }

        public void setMessage(String message) {
                mMessage = message;
        }
}