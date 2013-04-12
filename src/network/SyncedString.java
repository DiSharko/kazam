package network;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncedString {
	
	private String _data;
	private Lock _lock;
	
	public SyncedString(){
		_data = null;
		_lock = new ReentrantLock();
	}
	
	public String getData() {
		_lock.lock();
		String data = _data;
		_lock.unlock();
		return data;
	}
	
	public void setData(String data) {
		_lock.lock();
		_data = data;
		_lock.unlock();
	}

}
