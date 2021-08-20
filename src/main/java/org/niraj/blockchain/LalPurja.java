/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.niraj.blockchain;

/**
 *
 * @author Dell
 */
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class LalPurja {
    public String ownerName;
    public String ctznshipNo;
    public PrivateKey privateKey;
    public PublicKey publicKey;
    
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    
    

    public LalPurja(String ownerName, String ctznshipNo) {
        generateKeyPair();
        this.ownerName = ownerName;
        this.ctznshipNo = ctznshipNo;
    }
    
    public void generateKeyPair(){
        //we use EDSA.
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); //sha1 random number generator
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
	    // Set the public and private keys from the keyPair
	    privateKey = keyPair.getPrivate();
	    publicKey = keyPair.getPublic();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public int getLandsCount(){
        int count = 0;
        for(Map.Entry<String, TransactionOutput> item: BlockChain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)){
                count++;
            }
        }
        return count;
    }
    
    public Transaction passLand(PublicKey _receiver, Land land){
        if(!this.publicKey.equals(BlockChain.purjas.get(0).publicKey)){
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        
	for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            inputs.add(new TransactionInput(UTXO.id));
	}
        
        
        Transaction newTransaction = new Transaction(publicKey, _receiver , land, inputs);
	newTransaction.generateSignature(privateKey);
	
        for(TransactionInput input: inputs){
            UTXOs.remove(input.transactionOutputId);
            
	}
	return newTransaction;
    }
    
    
}
