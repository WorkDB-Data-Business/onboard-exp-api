package br.com.harvest.onboardexperience.utils;

public class SQLQueryUtils {
	
	public static final String IS_ACTIVE_FILTER   		  = "is_active = true";
	public static final String SOFT_DELETE_REWARD 		  = "UPDATE tbreward SET is_active = false WHERE idreward = ?";
	public static final String SOFT_DELETE_CLIENT		  = "UPDATE tbclient SET is_active = false WHERE idclient = ?";
	public static final String SOFT_DELETE_COIN   		  = "UPDATE tbcoin SET is_active = false WHERE idcoin = ?";
	public static final String SOFT_DELETE_USER			  = "UPDATE tbuser SET is_active = false WHERE iduser = ?";
	public static final String SOFT_DELETE_COMPANY_ROLE   = "UPDATE tbcompany_role SET is_active = false WHERE idcompany_role = ?";
	public static final String SOFT_DELETE_GROUP   		  = "UPDATE tbgroup SET is_active = false WHERE idgroup = ?";
	public static final String SOFT_DELETE_LINK   		  = "UPDATE tblink SET is_active = false WHERE idlink = ?";

}
