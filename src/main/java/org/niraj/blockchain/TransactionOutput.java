/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.niraj.blockchain;

import java.security.PublicKey;

/**
 *
 * @author Dell
 */
public class TransactionOutput {
    public String id;
    public PublicKey receiver;
    public Land land;
    public String parentTransactionId;

    public TransactionOutput(PublicKey receiver, Land land, String parentTransactionId) {
        this.receiver = receiver;
        this.land = land;
        this.parentTransactionId = parentTransactionId;
        this.id = HashUtil.applySha256(HashUtil.getStringFromKey(receiver) + land + parentTransactionId);
    }
    
    public boolean isMine(PublicKey publicKey){
        return (publicKey == receiver);
    }
    
    
}
