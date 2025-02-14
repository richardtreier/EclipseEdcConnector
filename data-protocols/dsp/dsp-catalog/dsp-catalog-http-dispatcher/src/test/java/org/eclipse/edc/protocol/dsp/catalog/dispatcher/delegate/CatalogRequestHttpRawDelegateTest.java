/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.protocol.dsp.catalog.dispatcher.delegate;

import okhttp3.ResponseBody;
import org.eclipse.edc.catalog.spi.CatalogRequestMessage;
import org.eclipse.edc.protocol.dsp.spi.dispatcher.DspHttpDispatcherDelegate;
import org.eclipse.edc.protocol.dsp.spi.testfixtures.dispatcher.DspHttpDispatcherDelegateTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CatalogRequestHttpRawDelegateTest extends DspHttpDispatcherDelegateTestBase<CatalogRequestMessage> {

    private CatalogRequestHttpRawDelegate delegate;

    @BeforeEach
    void setUp() {
        delegate = new CatalogRequestHttpRawDelegate();
    }

    @Test
    void parseResponse_returnCatalog() throws IOException {
        var responseBody = mock(ResponseBody.class);
        var response = dummyResponseBuilder(200).body(responseBody).build();
        var bytes = "test".getBytes();

        when(responseBody.bytes()).thenReturn(bytes);
        when(responseBody.byteStream()).thenReturn(new ByteArrayInputStream(bytes));

        var result = delegate.parseResponse().apply(response);

        assertThat(result).isEqualTo(bytes);
    }

    @Test
    void parseResponse_responseBodyNull_throwException() {
        testParseResponse_shouldThrowException_whenResponseBodyNull();
    }

    @Override
    protected DspHttpDispatcherDelegate<?> delegate() {
        return delegate;
    }

}
