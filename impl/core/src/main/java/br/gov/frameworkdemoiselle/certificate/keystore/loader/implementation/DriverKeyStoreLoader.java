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

package br.gov.frameworkdemoiselle.certificate.keystore.loader.implementation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import br.gov.frameworkdemoiselle.certificate.keystore.loader.DriverNotAvailableException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.InvalidPinException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoader;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.KeyStoreLoaderException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.PKCS11NotFoundException;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.configuration.Configuration;
import br.gov.frameworkdemoiselle.certificate.keystore.loader.configuration.LoaderConfig;

/**
 * Implementação de KeyStoreLoader baseado em drivers do sistema operacional. É
 * necessário informar o arquivo do driver no sistema operacional e o nome da
 * API.
 */
public class DriverKeyStoreLoader implements KeyStoreLoader {

	private CallbackHandler callback;

	@Override
	public KeyStore getKeyStore() {
		String configFile = Configuration.getInstance().getPKCS11ConfigFile();

		if (configFile != null)
			return this.getKeyStoreFromConfigFile(configFile);
		else
			return this.getKeyStoreFromDrivers();
	}

	public KeyStore getKeyStoreFromDriver(String driverPath) {

		String driverName = driverPath.replaceAll("\\\\", "/");
		int begin = driverName.lastIndexOf("/");
		begin = begin <= -1 ? 0 : begin + 1;
		int end = driverName.length();
		driverName = driverName.substring(begin, end);

		return this.getKeyStoreFromDriver(driverName, driverPath);

	}

	public KeyStore getKeyStoreFromDriver(String driverName, String driverPath) {
		Configuration.getInstance().addDriver(driverName, driverPath);
		String pkcs11ConfigSettings = null;
		KeyStore keyStore = null;
		pkcs11ConfigSettings = LoaderConfig.PKCS11_CONTENT_CONFIG_FILE.getValue(driverName, driverPath);
		byte[] pkcs11ConfigBytes = pkcs11ConfigSettings.getBytes();
		ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11ConfigBytes);

		try {
			Constructor<?> construtor = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(new Class[] { InputStream.class });
			Provider pkcs11Provider = (Provider) construtor.newInstance(new Object[] { confStream });
			Security.addProvider(pkcs11Provider);
			confStream.close();
			Method login = Class.forName("sun.security.pkcs11.SunPKCS11").getMethod("login", new Class[] { Subject.class, CallbackHandler.class });
			login.invoke(Security.getProvider(pkcs11Provider.getName()), new Object[] { null, this.callback });
			keyStore = KeyStore.getInstance(LoaderConfig.PKCS11_KEYSTORE_TYPE.getValue(), pkcs11Provider.getName());
			keyStore.load(null, null);

		} catch (Exception e) {
			if (e.getCause().toString().equals("javax.security.auth.login.FailedLoginException"))
				throw new InvalidPinException(LoaderConfig.PINNUMBER_INVALID.getValue(), e);

			if (e.getCause().toString().equals("javax.security.auth.login.LoginException"))
				throw new InvalidPinException(LoaderConfig.PINNUMBER_INVALID.getValue(), e);
			else
				throw new PKCS11NotFoundException(LoaderConfig.MODULE_LOAD_ERROR.getValue(), e);
		}
		return keyStore;
	}

	private KeyStore getKeyStoreFromConfigFile(String configFile) {

		KeyStore keyStore = null;

		try {
			Constructor<?> construtor = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(new Class[] { String.class });
			Provider pkcs11Provider = (Provider) construtor.newInstance(new Object[] { configFile });
			Security.addProvider(pkcs11Provider);
			Method login = Class.forName("sun.security.pkcs11.SunPKCS11").getMethod("login", new Class[] { Subject.class, CallbackHandler.class });
			login.invoke(Security.getProvider(pkcs11Provider.getName()), new Object[] { null, this.callback });
			keyStore = KeyStore.getInstance(LoaderConfig.PKCS11_KEYSTORE_TYPE.getValue(), pkcs11Provider.getName());
			keyStore.load(null, null);

		} catch (Exception e) {
			if (e.getCause().toString().equals("javax.security.auth.login.FailedLoginException"))
				throw new InvalidPinException(LoaderConfig.PINNUMBER_INVALID.getValue(), e);

			if (e.getCause().toString().equals("javax.security.auth.login.LoginException"))
				throw new InvalidPinException(LoaderConfig.PINNUMBER_INVALID.getValue(), e);
			else
				throw new PKCS11NotFoundException(LoaderConfig.MODULE_LOAD_ERROR.getValue(), e);
		}
		return keyStore;
	}

	private KeyStore getKeyStoreFromDrivers() {
		KeyStoreLoaderException error = new KeyStoreLoaderException(LoaderConfig.DRIVERS_NOT_COMPATIBLE.getValue());
		Map<String, String> drivers = Configuration.getInstance().getDrivers();

		if (drivers == null || drivers.isEmpty()) {
			throw new DriverNotAvailableException(LoaderConfig.DRIVERS_EMPTY.getValue());
		}

		Set<String> keyDrivers = drivers.keySet();
		KeyStore keyStore = null;

		for (String driver : keyDrivers) {
			try {
				String urlDriver = drivers.get(driver);
				keyStore = this.getKeyStoreFromDriver(driver, urlDriver);
				break;
			} catch (PKCS11NotFoundException e) {
				error.addError(e);
			} catch (InvalidPinException e) {
				throw e;
			} catch (Throwable erro) {
				error.addError(erro);
			}
		}

		if (keyStore == null && error.hasErrors()) {
			throw error;
		}

		return keyStore;
	}

	@Override
	public void setCallbackHandler(CallbackHandler callback) {
		this.callback = callback;
	}

}