package org.jrm.healthAdmin.utils;

// import org.hibernate.engine.spi.SharedSessionContractImplementor;
// import org.hibernate.id.UUIDGenerator;
// import org.jrm.backend.models.HasUuid;
// import org.jrm.backend.models.Patient;

// public class CustomUuidGenerator extends UUIDGenerator {
//     @Override
//     public Object generate(SharedSessionContractImplementor session, Object object) {
//         if (object instanceof HasUuid entity) {
//             if (entity.getId() != null) {
//                 // Use the existing ID if it's already set
//                 return entity.getId();
//             }
//         }
//         // Generate a new UUID if the ID is not set
//         return super.generate(session, object);
//     }
// }
