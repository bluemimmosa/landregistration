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
import java.util.ArrayList;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey receiver;
    public Land land;
    public byte[] signature;
    
    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();
    
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, Land land, ArrayList<TransactionInput>inputs) {
        this.sender = from;
        this.receiver = to;
        this.land = land;
        this.inputs = inputs;
    }
    
    
    private String calculateHash(){
        sequence++;
        return HashUtil.applySha256(
                HashUtil.getStringFromKey(sender)+
                HashUtil.getStringFromKey(receiver)+
                land+
                sequence
        );
    }
    
    public void generateSignature(PrivateKey privateKey){
        String data = HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(receiver) + land;
        signature = HashUtil.applyECDSASig(privateKey, data);
    }
    
    public boolean verifySignature(){
        String data = HashUtil.getStringFromKey(sender) + HashUtil.getStringFromKey(receiver) + land;
        return HashUtil.verifyECDSASig(sender, data, signature);
    }
    
    public boolean processTransaction(){
        if(verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
	}
				
	//gather transaction inputs (Make sure they are unspent):
	for(TransactionInput i : inputs) {
            i.UTXO = BlockChain.UTXOs.get(i.transactionOutputId);
        }
		
	//generate transaction outputs:
	//float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
	transactionId = calculateHash();
	outputs.add(new TransactionOutput( this.receiver, land,transactionId)); //send value to recipient
	outputs.add(new TransactionOutput( this.sender, null,transactionId)); //send the left over 'change' back to sender		
				
	//add outputs to Unspent list
	for(TransactionOutput o : outputs) {
            BlockChain.UTXOs.put(o.id , o);
	}
		
	//remove transaction inputs from UTXO lists as spent:
	for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it 
            BlockChain.UTXOs.remove(i.UTXO.id);
	}
		
    return true;
    }
    
    
}
