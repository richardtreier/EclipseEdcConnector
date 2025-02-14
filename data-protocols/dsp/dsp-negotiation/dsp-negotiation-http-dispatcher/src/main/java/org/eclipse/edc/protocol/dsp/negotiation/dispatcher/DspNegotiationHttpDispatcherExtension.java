/*
 *  Copyright (c) 2023 Fraunhofer Institute for Software and Systems Engineering
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Fraunhofer Institute for Software and Systems Engineering - initial API and implementation
 *
 */

package org.eclipse.edc.protocol.dsp.negotiation.dispatcher;

import org.eclipse.edc.connector.contract.spi.types.agreement.ContractAgreementMessage;
import org.eclipse.edc.connector.contract.spi.types.agreement.ContractAgreementVerificationMessage;
import org.eclipse.edc.connector.contract.spi.types.agreement.ContractNegotiationEventMessage;
import org.eclipse.edc.connector.contract.spi.types.negotiation.ContractNegotiationTerminationMessage;
import org.eclipse.edc.connector.contract.spi.types.negotiation.ContractOfferMessage;
import org.eclipse.edc.connector.contract.spi.types.negotiation.ContractRequestMessage;
import org.eclipse.edc.jsonld.spi.JsonLd;
import org.eclipse.edc.protocol.dsp.dispatcher.PostDspHttpRequestFactory;
import org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate.ContractAgreementMessageHttpDelegate;
import org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate.ContractAgreementVerificationMessageHttpDelegate;
import org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate.ContractNegotiationEventMessageHttpDelegate;
import org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate.ContractNegotiationTerminationMessageHttpDelegate;
import org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate.ContractOfferMessageHttpDelegate;
import org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate.ContractRequestMessageHttpDelegate;
import org.eclipse.edc.protocol.dsp.spi.dispatcher.DspHttpRemoteMessageDispatcher;
import org.eclipse.edc.protocol.dsp.spi.serialization.JsonLdRemoteMessageSerializer;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.types.TypeManager;

import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.AGREEMENT;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.BASE_PATH;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.CONTRACT_OFFER;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.CONTRACT_REQUEST;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.EVENT;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.INITIAL_CONTRACT_REQUEST;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.TERMINATION;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.VERIFICATION;
import static org.eclipse.edc.spi.CoreConstants.JSON_LD;

@Extension(value = DspNegotiationHttpDispatcherExtension.NAME)
public class DspNegotiationHttpDispatcherExtension implements ServiceExtension {

    public static final String NAME = "Dataspace Protocol Negotiation HTTP Dispatcher Extension";

    @Inject
    private DspHttpRemoteMessageDispatcher messageDispatcher;
    @Inject
    private TypeManager typeManager;
    @Inject
    private JsonLdRemoteMessageSerializer remoteMessageSerializer;
    @Inject
    private JsonLd jsonLdService;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        messageDispatcher.registerMessage(
                ContractAgreementMessage.class,
                new PostDspHttpRequestFactory<>(remoteMessageSerializer, m -> BASE_PATH + m.getProcessId() + AGREEMENT),
                new ContractAgreementMessageHttpDelegate()
        );
        messageDispatcher.registerMessage(
                ContractAgreementVerificationMessage.class,
                new PostDspHttpRequestFactory<>(remoteMessageSerializer, m -> BASE_PATH + m.getProcessId() + AGREEMENT + VERIFICATION),
                new ContractAgreementVerificationMessageHttpDelegate()
        );
        messageDispatcher.registerMessage(
                ContractNegotiationEventMessage.class,
                new PostDspHttpRequestFactory<>(remoteMessageSerializer, m -> BASE_PATH + m.getProcessId() + EVENT),
                new ContractNegotiationEventMessageHttpDelegate()
        );
        messageDispatcher.registerMessage(
                ContractNegotiationTerminationMessage.class,
                new PostDspHttpRequestFactory<>(remoteMessageSerializer, m -> BASE_PATH + m.getProcessId() + TERMINATION),
                new ContractNegotiationTerminationMessageHttpDelegate()
        );
        messageDispatcher.registerMessage(
                ContractRequestMessage.class,
                new PostDspHttpRequestFactory<>(remoteMessageSerializer, m -> {
                    if (m.getType() == ContractRequestMessage.Type.INITIAL) {
                        return BASE_PATH + INITIAL_CONTRACT_REQUEST;
                    } else {
                        return BASE_PATH + m.getProcessId() + CONTRACT_REQUEST;
                    }
                }),
                new ContractRequestMessageHttpDelegate(typeManager.getMapper(JSON_LD), jsonLdService)
        );
        messageDispatcher.registerMessage(
                ContractOfferMessage.class,
                new PostDspHttpRequestFactory<>(remoteMessageSerializer, m -> BASE_PATH + m.getProcessId() + CONTRACT_OFFER),
                new ContractOfferMessageHttpDelegate()
        );
    }
}
