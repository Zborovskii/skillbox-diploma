package DTO;

public class SuccessResultDTO {
    Boolean result;
    Error error;

    public SuccessResultDTO(Boolean result, Error error) {
        this.result = result;
        this.error = error;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
