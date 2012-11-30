package com.michaelpardo.android.net;

import java.util.ArrayList;
import java.util.List;

public abstract class ResponseBase {
        private List<ServerError> mErrors;

        public final List<ServerError> getErrors() {
                return mErrors;
        }

        public final void setErrors(List<ServerError> errors) {
                mErrors = errors;
        }

        public final void addError(ServerError error) {
                if (mErrors == null) {
                        mErrors = new ArrayList<ServerError>();
                }

                mErrors.add(error);
        }

        public final boolean hasErrors() {
                return mErrors != null && mErrors.size() > 0;
        }

        public String getBestErrorMessage() {
                if (hasErrors()) {
                        return mErrors.get(0).getMessage();
                }

                return null;
        }
}
