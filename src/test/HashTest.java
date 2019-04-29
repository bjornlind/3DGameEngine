package test;

import java.net.InetSocketAddress;

public class HashTest {
	
	public static void main(String[] args) {
		
		InetSocketAddress address1 = new InetSocketAddress("31.211.243.51", 1);
		InetSocketAddress address2 = new InetSocketAddress("31.211.243.51", 2);
		InetSocketAddress address3 = new InetSocketAddress("31.211.243.51", 3);
		
		System.out.println(address1.hashCode());
		System.out.println(address2.hashCode());
		System.out.println(address3.hashCode());
		System.out.println(address2.hashCode() == address3.hashCode());
		
	}

}
