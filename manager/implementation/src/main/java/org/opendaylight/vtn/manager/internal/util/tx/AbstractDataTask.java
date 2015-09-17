/*
 * Copyright (c) 2015 NEC Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.vtn.manager.internal.util.tx;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import org.opendaylight.vtn.manager.VTNException;

import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;

import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.types.rev150209.VtnErrorTag;

/**
 * An abstract implementation of
 * {@link org.opendaylight.vtn.manager.internal.TxTask} used to update
 * a data object in the MD-SAL datastore.
 *
 * @param <D>  The type of the target data object in MD-SAL datastore.
 * @param <V>  The type of the object to be returned by the task.
 */
public abstract class AbstractDataTask<D extends DataObject, V>
    extends AbstractTxTask<V> {

    /**
     * A set of {@link VtnErrorTag} that indicate an error caused by a
     * bad request.
     */
    private static final Set<VtnErrorTag>  BAD_REQUEST_TAGS;

    /**
     * The target type of the MD-SAL datastore.
     */
    private final LogicalDatastoreType  storeType;

    /**
     * The instance identifier that specifies the target data object
     * in the MD-SAL datastore.
     */
    private final InstanceIdentifier<D>  targetPath;

    /**
     * Initialize static fields.
     */
    static {
        Set<VtnErrorTag> set = EnumSet.of(
            VtnErrorTag.BADREQUEST, VtnErrorTag.NOTFOUND,
            VtnErrorTag.CONFLICT);
        BAD_REQUEST_TAGS = ImmutableSet.copyOf(set);
    }

    /**
     * Construct a new instance.
     *
     * @param store  The target type of the MD-SAL datastore.
     * @param path   The path to the target object.
     */
    public AbstractDataTask(LogicalDatastoreType store,
                            InstanceIdentifier<D> path) {
        storeType = store;
        targetPath = path;
    }

    /**
     * Return the instance identifier that specifies the target data object
     * in the MD-SAL datastore.
     *
     * @return  An {@link InstanceIdentifier} instance.
     */
    public final InstanceIdentifier<D> getTargetPath() {
        return targetPath;
    }

    /**
     * Return the target type of the MD-SAL datastore.
     *
     * @return  A {@link LogicalDatastoreType} instance.
     */
    public final LogicalDatastoreType getDatastoreType() {
        return storeType;
    }

    // AbstractTxTask

    /**
     * Determine whether the transaction queue should log the given error
     * or not.
     *
     * <p>
     *   This method returns {@code false} if a {@link VTNException} is
     *   passed and it seems to be caused by a bad request.
     * </p>
     *
     * @param t  A {@link Throwable} that is going to be thrown.
     * @return   {@code true} if the transaction queue should log the given
     *           {@link Throwable}. Otherwise {@code false}.
     */
    @Override
    public boolean needErrorLog(Throwable t) {
        if (t instanceof VTNException) {
            VTNException e = (VTNException)t;
            return !BAD_REQUEST_TAGS.contains(e.getVtnErrorTag());
        }

        return true;
    }
}
