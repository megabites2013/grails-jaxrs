/*
 * Copyright 2009, 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.plugins.jaxrs

import org.grails.plugins.jaxrs.core.JaxrsContext
import org.grails.plugins.jaxrs.core.JaxrsRequestWrapper
import org.grails.plugins.jaxrs.core.JaxrsUtil

import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

/**
 * Dispatches HTTP requests to one of the JAX-RS implementations supported by
 * the plugin.
 *
 * @author Martin Krasser
 */
class JaxrsController {
    /**
     * Default action to route requests to.
     */
    static defaultAction = 'handle'

    /**
     * JAX-RS context instance.
     */
    JaxrsContext jaxrsContext

    /**
     * Helper util.
     */
    JaxrsUtil jaxrsUtil

    /**
     * Catch-all handler for JAX-RS requests.
     *
     * @return
     */
    def handle() {
        // Grails provides a wrapped response object to controllers for doing
        // Sitemesh-specific page rendering. This will, by default, take place
        // when the response content type is 'text/html'. For 'text/html'
        // responses, generated by a JAX-RS implementation, we want to avoid
        // Sitemesh-specific page rendering and therefore operate on the raw
        // servlet request.
        ServletResponse unwrapped = jaxrsUtil.unwrap(response)

        // Let the JAX-RS implementation process the request
        jaxrsContext.process(new JaxrsRequestWrapper(request), unwrapped as HttpServletResponse)

        // Marks the response as committed
        unwrapped.outputStream.flush()

        // Response already written, nothing to return
        return null
    }
}

