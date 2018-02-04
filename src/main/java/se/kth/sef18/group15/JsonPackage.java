package se.kth.sef18.group15;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.*;
import java.util.regex.*;
import com.google.gson.Gson;


public class JsonPackage{
    /*
        *Returns a long list of Objects of GitInfo
        * @return Object of GitInfo
    */
	public static GitInfo readURL(BufferedReader url){
		return (new Gson()).fromJson(url, GitInfo.class);

	} 

}
