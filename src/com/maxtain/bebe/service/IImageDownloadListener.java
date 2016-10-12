package com.maxtain.bebe.service;

import org.apache.http.NameValuePair;

public interface IImageDownloadListener {
	public void imageDownloadDoneCallback(NameValuePair result,long id,String category);
}