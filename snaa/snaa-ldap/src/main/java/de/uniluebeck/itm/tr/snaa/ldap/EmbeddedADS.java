package de.uniluebeck.itm.tr.snaa.ldap;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.util.HashSet;

import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.entry.ServerEntry;
import org.apache.directory.server.core.partition.Partition;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.xdbm.Index;
import org.apache.directory.shared.ldap.entry.Entry;
import org.apache.directory.shared.ldap.name.LdapDN;

/**
 * A simple example exposing how to embed Apache Directory Server
 * into an application.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class EmbeddedADS
{
    /** The directory service */
    private DirectoryService service;
    
    /**
     * Add a new partition to the server
     *
     * @param partitionId The partition Id
     * @param partitionDn The partition DN
     * @return The newly added partition
     * @throws Exception If the partition can't be added
     */
    private Partition addPartition( String partitionId, String partitionDn ) throws Exception
    {
        Partition partition = new JdbmPartition();
        partition.setId( partitionId );
        partition.setSuffix( partitionDn );
        service.addPartition( partition );
        
        return partition;
    }
    
    
    /**
     * Add a new set of index on the given attributes
     *
     * @param partition The partition on which we want to add index
     * @param attrs The list of attributes to index
     */
    private void addIndex( Partition partition, String... attrs )
    {
        // Index some attributes on the smartsantander partition
        HashSet<Index<?, ServerEntry>> indexedAttributes = new HashSet<Index<?, ServerEntry>>();
        
        for ( String attribute:attrs )
        {
            indexedAttributes.add( new JdbmIndex<String,ServerEntry>( attribute ) );
        }
        
        ((JdbmPartition)partition).setIndexedAttributes( indexedAttributes );
    }
    
    
    /**
     * Initialize the server. It creates the partition, adds the index, and
     * injects the context entries for the created partitions.
     *
     * @throws Exception if there were some problems while initializing the system
     */
    private void init() throws Exception
    {
        // Initialize the LDAP service
        service = new DefaultDirectoryService();
        
        // Disable the ChangeLog system
        service.getChangeLog().setEnabled( false );
        service.setDenormalizeOpAttrsEnabled( true );
      
        
        // Create some new partitions named 'foo', 'bar' and 'smartsantander'.
       // Partition fooPartition = addPartition( "foo", "dc=foo,dc=com" );
       // Partition barPartition = addPartition( "bar", "dc=bar,dc=com" );
        Partition smartsantanderPartition = addPartition( "smartsantander", "dc=smartsantander,dc=eu" );
        
        // Index some attributes on the smartsantander partition
        addIndex( smartsantanderPartition, "objectClass", "ou", "uid" );
        
        // create an LDAP server        
        LdapServer ldapService = new LdapServer();
        ldapService.setTransports(new TcpTransport(389));
        // And start the service
        service.startup();
        ldapService.setDirectoryService(service);
        ldapService.start();
        

        
        
        // Inject the smartsantander root entry
        if ( !service.getAdminSession().exists( smartsantanderPartition.getSuffixDn() ) )
        {
            LdapDN dnSmartsantander = new LdapDN( "dc=smartsantander,dc=eu" );
            ServerEntry entrySmartsantander = service.newEntry( dnSmartsantander );
            entrySmartsantander.add( "objectClass", "top", "domain", "extensibleObject" );
            entrySmartsantander.add( "dc", "smartsantander" );
            service.getAdminSession().add( entrySmartsantander );
        }
        
        // We are all done !
    }
    
    
    /**
     * Creates a new instance of EmbeddedADS. It initializes the directory service.
     *
     * @throws Exception If something went wrong
     */
    public EmbeddedADS() throws Exception
    {
        init();
    }

    /**
     * Main class. We just do a lookup on the server to check that it's available.
     *
     * @param args Not used. 
     */
    public static void main( String[] args ) //throws Exception 
    {
        try
        {
            // Create the server
            EmbeddedADS ads = new EmbeddedADS();
            
            // Read an entry
            Entry result = ads.service.getAdminSession().lookup( new LdapDN( "dc=smartsantander,dc=eu" ) );
            
            // And print it if available
            System.out.println( "Found entry : " + result );
        
        }
        catch ( Exception e )
        {
            // Ok, we have something wrong going on ...
            e.printStackTrace();
        }
    }
}