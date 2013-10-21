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
 * 
 */

package br.gov.frameworkdemoiselle.certificate.signer.factory;

import br.gov.frameworkdemoiselle.certificate.signer.SignerException;

/**
 * Fábrica abstrata que concentra a leitura das configurações para as fábricas
 * especializadas como também as funcionalidades de reflexão de classes.
 * 
 * @see {@link PKCS1Factory}, {@link PKCS7Factory}
 * 
 */
abstract public class GenericFactory<F> {

	private String className = null;

	/**
	 * Principal método da fábrica. Este método fabrica classes a partir de nome
	 * de classes definidos em variáveis de ambiente. Tais variáveis são
	 * definidas por cada fábrica concreta que implementar a fábrica abstrata
	 * através do método getVariableName(). Uma vez lido a variável de ambiente,
	 * o valor da variável é armazenada na propriedade "className". Caso a
	 * variável de ambiente não esteja setada, um objeto padrão é construído
	 * através do método abstrato factoryDefault().
	 */
	public F factory() {
		F result = null;

		if (this.className == null) {
			this.className = this.getContentFromVariables(getVariableName());
		}

		if (this.className != null && this.className.length() > 0) {
			result = this.factoryFromClassName(this.className);
		} else {
			result = this.factoryDefault();
		}

		return result;
	}

	/**
	 * Instancia um objeto a partir do nome de sua classe
	 */
	@SuppressWarnings("all")
	public F factoryFromClassName(String className) {
		F result = null;

		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (Throwable error) {
			throw new SignerException("Class [" + className + "] does not exist", error);
		}
		if (clazz != null) {
			try {
				result = (F) clazz.newInstance();
			} catch (Throwable error) {
				throw new SignerException("incompatible Class [" + clazz.getCanonicalName() + "]", error);
			}
		}

		return result;
	}

	/**
	 * Busca nas variáveis de ambiente ou em variável da JVM um determinado
	 * valor. Prioridade para as variáveis de ambiente.
	 * 
	 * @param key
	 *            Chave de localizacao da variável
	 * @return O conteúdo definida em uma das variáveis. NULL se nenhuma
	 *         variável for definida
	 */
	private String getContentFromVariables(String key) {
		String content = System.getenv(key);
		if (content == null)
			content = System.getenv(key.toLowerCase());
		if (content == null)
			content = System.getenv(key.toUpperCase());

		if (content == null)
			content = System.getProperty(key);
		if (content == null)
			content = System.getProperty(key.toLowerCase());
		if (content == null)
			content = System.getProperty(key.toUpperCase());

		return content;
	}

	/**
	 * Obriga a classe concreta a fabricar um objeto por padrão
	 */
	public abstract F factoryDefault();

	/**
	 * Toda fábrica concreta precisa definir em qual variável de ambiente contém
	 * o nome da classe a ser fabricada
	 */
	protected abstract String getVariableName();

}
