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

package org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate;

import org.eclipse.edc.connector.contract.spi.types.negotiation.ContractNegotiationTerminationMessage;
import org.eclipse.edc.protocol.dsp.spi.dispatcher.DspHttpDispatcherDelegate;
import org.eclipse.edc.protocol.dsp.spi.testfixtures.dispatcher.DspHttpDispatcherDelegateTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContractNegotiationTerminationMessageHttpDelegateTest extends DspHttpDispatcherDelegateTestBase<ContractNegotiationTerminationMessage> {

    private ContractNegotiationTerminationMessageHttpDelegate delegate;

    @BeforeEach
    void setUp() {
        delegate = new ContractNegotiationTerminationMessageHttpDelegate();
    }

    @Test
    void parseResponse_returnNull() {
        testParseResponse_shouldReturnNullFunction_whenResponseBodyNotProcessed();
    }

    @Override
    protected DspHttpDispatcherDelegate<?> delegate() {
        return delegate;
    }
}
