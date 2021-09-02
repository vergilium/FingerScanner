package ua.ks.hogo.fingerscanner;

public class Fingerprint {
    public Long id;
    public byte[] fingerTemplate;
    public Integer ownerId;
    public Integer authFilialId;
    public String operator;
    public Boolean employee;
    public Boolean matchResult;
}
