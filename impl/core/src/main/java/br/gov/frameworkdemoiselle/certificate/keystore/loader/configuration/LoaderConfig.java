/*
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 * ----------------------------------------------------------------------------
 * This file is part of Demoiselle Framework.
 * 
 * Demoiselle Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 * ----------------------------------------------------------------------------
 * Este arquivo é parte do Framework Demoiselle.
 * 
 * O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 * modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 * do Software Livre (FSF).
 * 
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 * GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 * APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 * para maiores detalhes.
 * 
 * Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 * "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 * ou escreva para a Fundação do Software Livre (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */

package br.gov.frameworkdemoiselle.certificate.keystore.loader.configuration;

import java.applet.Applet;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Enum com todas as chaves e valores das configurações do componente.
 * 
 * Os valores das chaves são obtidos ou nos parâmetros da applet ou no arquivo
 * de configuração security-applet.properties do projeto desenvolvido com este
 * componente ou no security-applet-default.properties caso não seja informado
 * nenhum outro resource.
 * 
 */
public enum LoaderConfig {

	NAME_NULL("error.nameNull"),
	PATH_NULL("error.pathNull"),
	PATH_INVALID("error.pathInvalid"),
	DRIVER_ERROR_LOAD("error.driverLoadError"),
	DRIVER_ERROR_LOAD_VARIABLE("error.loadVariableError"),
	KEY_JAVA_VERSION("key.javaVersion"),
	KEY_OS_NAME("key.osName"),
	VAR_PKCS11_CONFIG("var.pkcs11Config"),
	VAR_PKCS11_DRIVER("var.pkcs11Driver"),
	CUSTOM_CONFIG_PATH("var.customConfigPath"),
	CUSTOM_CONFIG_FILENAME("var.customConfigFilename"),
	FILE_SEPARATOR("key.fileSeparator"),
	MSCAPI_DISABLED("var.mscapiDisabled"),
	FILE_NAME_REQUIRED("error.fileNameRequired"),
	PINNUMBER_INVALID("error.pinInvalid"),
	MODULE_LOAD_ERROR("error.moduleLoadError"),
	DRIVERS_EMPTY("error.driverEmpty"),
	DRIVERS_NOT_COMPATIBLE("error.driverNotCompatible"),
	PKCS11_KEYSTORE_TYPE("var.keystoreType"),
	PKCS11_CONTENT_CONFIG_FILE("var.pkcs11Content");
	

	private String key;
	private static ResourceBundle rb;
	private static Applet applet;

	/**
	 * Construtor privado recebendo a chave do enum
	 * 
	 * @param key
	 */
	private LoaderConfig(String key) {
		this.key = key;
	}

	/**
	 * Retorna o valor de enum para uma determinada chave Primeiramente é
	 * verificado se a chave foi informada como parâmetro do applet, se não, é
	 * obtida a chave no resource bundle que pode ser da aplicacao ou caso nao
	 * seja informado sera utilizado o resouce default do componente.
	 * 
	 * @return
	 */

	public String getValue() {
		try {
			String value = null;
			if (applet != null) {
				value = applet.getParameter(key);
			}
			if (value == null) {
				value = getResourceBundle().getString(key);
			}
			return value;
		} catch (MissingResourceException mre) {
			throw new RuntimeException("key '" + key + "' not found");
		}
	}
	
	public String getValue(Object... args) {
		return MessageFormat.format(getValue(), args);
	}

	/**
	 * Retorna o valor de enum convertido para o tipo 'int' conforme sua
	 * respectiva chave
	 * 
	 * @return
	 */
	public int getValueInt() {
		return Integer.valueOf(getValue());
	}

	/**
	 * Retorna o valor de enum convertido para o tipo 'boolean' conforme sua
	 * respectiva chave
	 * 
	 * @return
	 */
	public boolean getValueBoolean() {
		return Boolean.valueOf(getValue());
	}

	/**
	 * Retorna o resouceBundle utilizado para obtencao
	 * 
	 * @return
	 */
	private ResourceBundle getResourceBundle() {
		if (rb != null) {
			return rb;
		}
		try {
			rb = getBundle("keystore-loader");
		} catch (MissingResourceException mre) {
			try {
				rb = getBundle("keystore-loader-default");
			} catch (MissingResourceException e) {
				throw new RuntimeException("key '" + key
						+ "' not found for resource ''");
			}
		}
		return rb;
	}

	public ResourceBundle getBundle(String bundleName) {
		return ResourceBundle.getBundle(bundleName);
	}

	/**
	 * Retorna a chave do enum
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	public static void setApplet(Applet _applet) {
		applet = _applet;
	}

}
