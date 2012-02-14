package de.uniluebeck.itm.tr.snaa.ldap.test;

import java.io.File;
import java.util.List;

import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.server.protocol.shared.store.LdifFileLoader;
import org.apache.directory.shared.ldap.ldif.LdifEntry;

import org.apache.directory.shared.ldap.ldif.LdifUtils;
import org.apache.directory.shared.ldap.model.entry.Entry;
import org.apache.directory.shared.ldap.model.exception.LdapException;
import org.apache.directory.shared.ldap.model.ldif.LdifReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.tr.snaa.ldap.EmbeddedADS;



public class TestLdapApi {

    private static final int LDAP_PORT = 10389;
    private static final Logger log = LoggerFactory.getLogger(TestLdapApi.class);
    private static EmbeddedADS eads;
    
    @BeforeClass
    public static void setup() throws Exception{
        //eads = new EmbeddedADS(new File("target/server-work"), LDAP_PORT);
    }
    
    @AfterClass
    public static void teardown(){
        
    }
    
    @Test
    public void test() throws Exception {
        int i=0;
        i++;
        LdapConnection connection = new LdapNetworkConnection( "localhost" , LDAP_PORT);
        
         LdifReader reader = new LdifReader();
         List<org.apache.directory.shared.ldap.model.ldif.LdifEntry> entries = reader.parseLdifFile("src/test/resources/test.ldif");
        entries.size();
    }

}
