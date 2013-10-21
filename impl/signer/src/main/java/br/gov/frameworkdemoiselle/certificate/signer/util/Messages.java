package br.gov.frameworkdemoiselle.certificate.signer.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME_DEFAULT = "certificate-signer-messages-default";
	private static final String BUNDLE_NAME_CUSTOM = "certificate-signer-messages";

	private static ResourceBundle RESOURCE_BUNDLE;

	static {
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME_CUSTOM);
		} catch (MissingResourceException mre) {
			try {
				RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME_DEFAULT);
			} catch (MissingResourceException e) {
				throw new RuntimeException("key '" + BUNDLE_NAME_DEFAULT
						+ "' not found for resource ''");
			}
		}
	}

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getString(String key, Object... args) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), args);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}