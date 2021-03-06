package com.cybersource.ws.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.*;

public class IdentityTest{
    Properties merchantProperties;
    private MerchantConfig config;
    
    @Before
    public void setUp() throws Exception {
    	//Loading the properties file from src/test/resources
        Properties merchantProperties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test_cybs.properties");
		if (in == null) {
			throw new RuntimeException("Unable to load test_cybs.properties file");
		}
		merchantProperties.load(in);
	    config = new MerchantConfig(merchantProperties, merchantProperties.getProperty("merchantID"));
    }

    @Test
    public void testSetUpMerchant() throws InstantiationException, IllegalAccessException, SignException{
    	String keyAlias = "CN="+config.getMerchantID()+",SERIALNUMBER=400000009910179089277";
    	X509Certificate x509Cert = Mockito.mock(X509Certificate.class);
    	Principal principal =  Mockito.mock(Principal.class);
    	PrivateKey pkey = Mockito.mock(PrivateKey.class);
    	Mockito.when(x509Cert.getSubjectDN()).thenReturn(principal);
    	Mockito.when(principal.getName()).thenReturn(keyAlias);
    	Identity identity = new Identity(config,x509Cert,pkey);
    	assertEquals(identity.getName(), config.getMerchantID());
    	assertEquals(identity.getSerialNumber(), "400000009910179089277");
    	assertNotNull(identity.getPrivateKey());
    }
    
    @Test
    public void testsetUpServer() throws InstantiationException, IllegalAccessException, SignException{
    	String keyAlias = "CN=CyberSource_SJC_US,SERIALNUMBER=400000009910179089277";
    	X509Certificate x509Cert = Mockito.mock(X509Certificate.class);
    	Principal principal =  Mockito.mock(Principal.class);
    	Mockito.when(x509Cert.getSubjectDN()).thenReturn(principal);
    	Mockito.when(principal.getName()).thenReturn(keyAlias);
    	Identity identity = new Identity(config,x509Cert);
    	assertEquals(identity.getName(), "CyberSource_SJC_US");
    	assertEquals(identity.getSerialNumber(), "400000009910179089277");
    	assertNull(identity.getPrivateKey());
    }

}
