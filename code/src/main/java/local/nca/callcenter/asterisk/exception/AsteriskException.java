package local.nca.callcenter.asterisk.exception;

import local.nca.callcenter.core.exception.AppException;

public class AsteriskException extends AppException {

    public AsteriskException(AsteriskError error) {
        super(error.getCode(), error.getType(), error.getMessage());
    }

    public AsteriskException(AsteriskError error, Throwable cause) {
        super(error.getCode(), error.getType(), error.getMessage(), cause);
    }
}
