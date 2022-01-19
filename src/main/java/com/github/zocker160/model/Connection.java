package com.github.zocker160.model;

import java.net.Inet4Address;
import java.util.Objects;

public class Connection {
	private final Inet4Address ip;
	private final int port;
	
	public Connection(Inet4Address ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}
	
	public Inet4Address getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}

	
	@Override
	public String toString() {
		return "Connection [ip=" + ip + ", port=" + port + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(ip, port);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connection other = (Connection) obj;
		return Objects.equals(ip, other.ip) && port == other.port;
	}
}
