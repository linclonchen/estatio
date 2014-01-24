/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.dom.party;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PublishedObject.ChangeKind;
import org.apache.isis.applib.annotation.PublishedObject.PayloadFactory;
import org.apache.isis.applib.services.publish.EventPayload;

public class OrganisationChangedPayloadFactory implements PayloadFactory {

    @Override
    @Programmatic
    public EventPayload payloadFor(
            final Object changedObject, final ChangeKind changeKind) {
        return new OrganisationChangedPayload((Organisation) changedObject);
    }

}
