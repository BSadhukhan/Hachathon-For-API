package com.ninja.lms.config;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;


public class UserIDGenerator extends SequenceStyleGenerator {
	
	public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
	public static final String VALUE_PREFIX_DEFAULT = "";
	private String valuePrefix;

	public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
	public static final String NUMBER_FORMAT_DEFAULT = "%02d";
	private String numberFormat;
	
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
	    return valuePrefix + String.format(numberFormat, super.generate(session, object));
	}

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
	    
		super.configure(LongType.INSTANCE, params, serviceRegistry);
		System.out.println("VALUE_PREFIX_PARAMETER :: " + VALUE_PREFIX_PARAMETER + " :: NUMBER_FORMAT_PARAMETER :: " + NUMBER_FORMAT_PARAMETER);
	    valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER, params, VALUE_PREFIX_DEFAULT);
	    System.out.println("valuePrefix :: "+ valuePrefix);
	    numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT);
	    System.out.println("numberFormat :: "+ numberFormat);
	}
	
}
