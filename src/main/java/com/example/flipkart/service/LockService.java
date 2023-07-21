package com.example.flipkart.service;

import com.example.flipkart.LockInterface;
import com.example.flipkart.models.Lock;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LockService implements LockInterface {

    Map<String, Lock> lockMap = new HashMap<>();

    @Override
    synchronized public void lockId(String id) {
        if(lockMap.containsKey(id))
            throw new RuntimeException("Lock exists");
        lockId2(id);
    }

    @Override
    public void unlockId(String id) {
        if(!lockMap.containsKey(id))
            return;
        lockMap.remove(id);
    }

    private void lockId2(String id) {
        lockMap.put(id, new Lock(new Date()));
    }
}
