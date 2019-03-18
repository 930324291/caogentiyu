
package com.football.net.http.reponse;

public class Result {

    private boolean success;

    private int result;
    private String error;
    private int total;

    private String access_token;

    private boolean isUnique ;

    public Result() {
    }

    public Result(int code, String desc) {
        result = code;
        this.error = desc;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String desc) {
        this.error = desc;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", result=" + result +
                ", error='" + error + '\'' +
                ", total=" + total +
                ", access_token='" + access_token + '\'' +
                ", isUnique=" + isUnique +
                '}';
    }
}
