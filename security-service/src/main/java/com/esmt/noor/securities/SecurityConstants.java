package com.esmt.noor.securities;

public class SecurityConstants {
    public static final String SECRET = "secret";
    public static final long EXPIRATION_TIME_ACCESS_TOKEN = 2*30*24*60*60*1000; // 2 mois exprimé en secondes
    public static final long EXPIRATION_TIME_REFRESH_TOKEN = 50*366*24*60*60*1000; // 50ans exprimé en secondes
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
