package com.maxtain.bebe.account;

public interface IAccountListener {
	public enum ACCOUNT_STATUS {
		CANCEL, NO_NETWORK, DUPLICATE, DATA_OK, DATA_ERR, NO_USER, LOGON,UPDATE_DONE
	};

	public void callback(final ACCOUNT_STATUS as);
}
