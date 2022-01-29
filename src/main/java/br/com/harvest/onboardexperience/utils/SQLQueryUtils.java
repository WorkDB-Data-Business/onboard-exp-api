package br.com.harvest.onboardexperience.utils;

public class SQLQueryUtils {
	
	public static final String IS_ACTIVE_FILTER   		  		= "is_active = true";
	public static final String IS_IMAGE_PREVIEW   		  		= "is_image_preview = true";
	public static final String IS_NOT_IMAGE_PREVIEW   		  	= "is_image_preview = false";
	public static final String SOFT_DELETE_REWARD 		 		= "UPDATE tbreward SET is_active = false WHERE idreward = ?";
	public static final String SOFT_DELETE_CLIENT		 		= "UPDATE tbclient SET is_active = false WHERE idclient = ?";
	public static final String SOFT_DELETE_COIN   		 		= "UPDATE tbcoin SET is_active = false WHERE idcoin = ?";
	public static final String SOFT_DELETE_USER			 		= "UPDATE tbuser SET is_active = false WHERE iduser = ?";
	public static final String SOFT_DELETE_COMPANY_ROLE  		= "UPDATE tbcompany_role SET is_active = false WHERE idcompany_role = ?";
	public static final String SOFT_DELETE_GROUP   		 		= "UPDATE tbgroup SET is_active = false WHERE idgroup = ?";
	public static final String SOFT_DELETE_LINK   		 		= "UPDATE tblink SET is_active = false WHERE idlink = ?";
	public static final String SOFT_DELETE_SCORM   		 		= "UPDATE tbscorm SET is_active = false WHERE idscorm = ?";
	public static final String SOFT_DELETE_SCORM_REGISTRATION   = "UPDATE tbscorm_registration SET is_active = false WHERE idscorm_registration = ?";

}
