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

package br.gov.frameworkdemoiselle.certificate.extension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.frameworkdemoiselle.certificate.oid.OID_2_16_76_1_3_2;
import br.gov.frameworkdemoiselle.certificate.oid.OID_2_16_76_1_3_3;
import br.gov.frameworkdemoiselle.certificate.oid.OID_2_16_76_1_3_4;
import br.gov.frameworkdemoiselle.certificate.oid.OID_2_16_76_1_3_8;

/**
 * Implemented Class for ICP-BRASIL (DOC-ICP-04) "EQUIPAMENTO/APLICAÇÃO"
 * Certificates.
 * 
 * @see ICPBRSubjectAlternativeNames
 */

public class ICPBRCertificateEquipment {

	private OID_2_16_76_1_3_2 oID_2_16_76_1_3_2 = null;
	private OID_2_16_76_1_3_3 oID_2_16_76_1_3_3 = null;
	private OID_2_16_76_1_3_4 oID_2_16_76_1_3_4 = null;
	private OID_2_16_76_1_3_8 oID_2_16_76_1_3_8 = null;

	/**
	 * 
	 * @param oid1
	 *            -> 2.16.76.1.3.2 e conteúdo = nome do responsável pelo
	 *            certificado
	 * 
	 * @param oid2
	 *            -> 2.16.76.1.3.3 e conteúdo = Cadastro Nacional de Pessoa
	 *            Juridica (CNPJ), se o certificado for de pessoa juridica
	 * 
	 * @param oid3
	 *            -> 2.16.76.1.3.4 e conteúdo = nas primeiras 8 (oito) posições,
	 *            a data de nascimento do responsável pelo certificado, no
	 *            formato ddmmaaaa; nas 11 (onze) posições subsequentes, o
	 *            Cadastro de Pessoa Fisica (CPF) do responsável; nas 11 (onze)
	 *            posições subsequentes, o número de Identificacao Social - NIS
	 *            (PIS, PASEP ou CI); nas 15 (quinze) posições subsequentes, o
	 *            número do RG do responsável; nas 6 (seis) posições
	 *            subsequentes, as siglas do orgao expedidor do RG e respectiva
	 *            UF
	 * 
	 * @param oid4
	 *            -> 2.16.76.1.3.8 e conteúdo = nome empresarial constante do
	 *            CNPJ (Cadastro Nacional de Pessoa Juridica), sem abreviacoes,
	 *            se o certificado for de pessoa juridica
	 * 
	 */
	public ICPBRCertificateEquipment(OID_2_16_76_1_3_2 oid1, OID_2_16_76_1_3_3 oid2, OID_2_16_76_1_3_4 oid3, OID_2_16_76_1_3_8 oid4) {
		this.oID_2_16_76_1_3_2 = oid1;
		this.oID_2_16_76_1_3_3 = oid2;
		this.oID_2_16_76_1_3_4 = oid3;
		this.oID_2_16_76_1_3_8 = oid4;
	}

	/**
	 * 
	 * @return string com o nome do responsável pelo certificado
	 */
	public String getNomeResponsavel() {
		return oID_2_16_76_1_3_2.getNome();
	}

	/**
	 * 
	 * @return nome empresarial constante do CNPJ (Cadastro Nacional de Pessoa
	 *         Juridica)
	 */
	public String getNomeEmpresarial() {
		return oID_2_16_76_1_3_8.getNome();
	}

	/**
	 * 
	 * @return número do CNPJ (Cadastro Nacional de Pessoa Juridica)
	 */
	public String getCNPJ() {
		return oID_2_16_76_1_3_3.getCNPJ();
	}

	/**
	 * 
	 * @return data de nascimento do responsável pelo certificado, no formato
	 *         Date
	 */
	public Date getDataNascimento() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			return sdf.parse(oID_2_16_76_1_3_4.getDataNascimento());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @return o número de Identificacao Social - NIS (PIS, PASEP ou CI)
	 */
	public String getNis() {
		return oID_2_16_76_1_3_4.getNIS();
	}

	/**
	 * 
	 * @return o número do RG do responsável
	 */
	public String getRg() {
		return oID_2_16_76_1_3_4.getRg();
	}

	/**
	 * 
	 * @return as siglas do orgao expedidor do RG
	 */
	public String getOrgaoExpedidorRg() {
		return oID_2_16_76_1_3_4.getOrgaoExpedidorRg();
	}

	/**
	 * 
	 * @return UF do orgao expedidor do RG
	 */
	public String getUfExpedidorRg() {
		return oID_2_16_76_1_3_4.getUfExpedidorRg();
	}
}
