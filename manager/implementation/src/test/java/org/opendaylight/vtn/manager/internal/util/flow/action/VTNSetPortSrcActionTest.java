/*
 * Copyright (c) 2015 NEC Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.vtn.manager.internal.util.flow.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import org.mockito.Mockito;

import org.opendaylight.vtn.manager.internal.util.packet.IcmpHeader;
import org.opendaylight.vtn.manager.internal.util.packet.TcpHeader;
import org.opendaylight.vtn.manager.internal.util.packet.UdpHeader;
import org.opendaylight.vtn.manager.internal.util.rpc.RpcErrorTag;
import org.opendaylight.vtn.manager.internal.util.rpc.RpcException;

import org.opendaylight.vtn.manager.internal.TestBase;
import org.opendaylight.vtn.manager.internal.XmlNode;
import org.opendaylight.vtn.manager.internal.XmlDataType;
import org.opendaylight.vtn.manager.internal.XmlValueType;

import org.opendaylight.controller.sal.utils.Status;
import org.opendaylight.controller.sal.utils.StatusCode;

import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.flow.action.rev150410.vtn.action.fields.VtnAction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.flow.action.rev150410.vtn.action.fields.vtn.action.VtnSetPortDstActionCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.flow.action.rev150410.vtn.action.fields.vtn.action.VtnSetPortSrcActionCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.flow.action.rev150410.vtn.action.fields.vtn.action.VtnSetPortSrcActionCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.flow.action.rev150410.vtn.action.fields.vtn.action.vtn.set.port.src.action._case.VtnSetPortSrcAction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.flow.action.rev150410.vtn.action.fields.vtn.action.vtn.set.port.src.action._case.VtnSetPortSrcActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.vtn.flow.action.rev150410.vtn.flow.action.list.VtnFlowActionBuilder;

import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.Action;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.SetTpDstActionCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.SetTpSrcActionCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.SetTpSrcActionCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.set.tp.src.action._case.SetTpSrcAction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.set.tp.src.action._case.SetTpSrcActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.ActionBuilder;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.PortNumber;

/**
 * JUnit test for {@link VTNSetPortSrcAction}.
 */
public class VTNSetPortSrcActionTest extends TestBase {
    /**
     * Root XML element name associated with {@link VTNSetPortSrcAction} class.
     */
    private static final String  XML_ROOT = "vtn-set-port-src-action";

    /**
     * Return a list of {@link XmlDataType} instances that specifies XML node
     * types mapped to a {@link VTNSetPortSrcAction} instance.
     *
     * @param name    The name of the target node.
     * @param parent  Path to the parent node.
     * @return  A list of {@link XmlDataType} instances.
     */
    public static List<XmlDataType> getXmlDataTypes(String name,
                                                    String ... parent) {
        ArrayList<XmlDataType> dlist = new ArrayList<>();
        Collections.addAll(
            dlist,
            new XmlValueType("order", Integer.class).add(name).prepend(parent),
            new XmlValueType("port", int.class).add(name).prepend(parent));
        return dlist;
    }

    /**
     * Test case for constructors and instance methods.
     *
     * <ul>
     *   <li>{@link VTNSetPortSrcAction#VTNSetPortSrcAction(int)}</li>
     *   <li>{@link VTNSetPortSrcAction#VTNSetPortSrcAction(org.opendaylight.vtn.manager.flow.action.SetTpSrcAction, int)}</li>
     *   <li>{@link VTNSetPortSrcAction#VTNSetPortSrcAction(VtnSetPortSrcActionCase, Integer)}</li>
     *   <li>{@link VTNSetPortSrcAction#set(VtnFlowActionBuilder)}</li>
     *   <li>{@link VTNSetPortSrcAction#set(ActionBuilder)}</li>
     *   <li>{@link VTNSetPortSrcAction#toFlowAction(VtnAction)}</li>
     *   <li>{@link VTNSetPortSrcAction#toVtnAction(Action)}</li>
     *   <li>{@link VTNSetPortSrcAction#getDescription(Action)}</li>
     *   <li>{@link VTNPortAction#getPort()}</li>
     *   <li>{@link VTNPortAction#getPortNumber()}</li>
     *   <li>{@link VTNPortAction#verifyImpl()}</li>
     *   <li>{@link FlowFilterAction#verify()}</li>
     *   <li>{@link FlowFilterAction#getIdentifier()}</li>
     *   <li>{@link VTNFlowAction#toVtnFlowActionBuilder(Integer)}</li>
     *   <li>{@link VTNFlowAction#toActionBuilder(Integer)}</li>
     * </ul>
     *
     * @throws Exception  An error occurred.
     */
    @Test
    public void testGetSet() throws Exception {
        int[] ports = {
            0, 1, 10, 33, 1234, 32767, 32768, 40000, 55555, 65535,
        };
        Integer[] orders = {
            null, Integer.MIN_VALUE, -1000, -1,
            0, 1, 2, 32000, Integer.MAX_VALUE,
        };

        VtnSetPortSrcActionCaseBuilder vacBuilder =
            new VtnSetPortSrcActionCaseBuilder();
        org.opendaylight.vtn.manager.flow.action.SetTpSrcAction vad;
        for (Integer order: orders) {
            for (int port: ports) {
                PortNumber pnum = new PortNumber(port);
                vad = new org.opendaylight.vtn.manager.flow.action.
                    SetTpSrcAction(port);
                VtnSetPortSrcAction vact = new VtnSetPortSrcActionBuilder().
                    setPort(pnum).build();
                VtnSetPortSrcActionCase vac = vacBuilder.
                    setVtnSetPortSrcAction(vact).build();
                SetTpSrcAction ma = new SetTpSrcActionBuilder().
                    setPort(pnum).build();
                SetTpSrcActionCase mact = new SetTpSrcActionCaseBuilder().
                    setSetTpSrcAction(ma).build();

                VTNSetPortSrcAction va;
                Integer anotherOrder;
                if (order == null) {
                    va = new VTNSetPortSrcAction(port);
                    anotherOrder = 0;
                } else {
                    va = new VTNSetPortSrcAction(vad, order);
                    assertEquals(order, va.getIdentifier());
                    assertEquals(port, va.getPort());
                    assertEquals(pnum, va.getPortNumber());

                    va = new VTNSetPortSrcAction(vac, order);
                    anotherOrder = order.intValue() + 1;
                }
                assertEquals(order, va.getIdentifier());
                assertEquals(port, va.getPort());
                assertEquals(pnum, va.getPortNumber());

                VtnFlowActionBuilder vbuilder =
                    va.toVtnFlowActionBuilder(anotherOrder);
                assertEquals(anotherOrder, vbuilder.getOrder());
                assertEquals(vac, vbuilder.getVtnAction());
                assertEquals(order, va.getIdentifier());

                ActionBuilder mbuilder = va.toActionBuilder(anotherOrder);
                assertEquals(anotherOrder, mbuilder.getOrder());
                assertEquals(mact, mbuilder.getAction());
                assertEquals(order, va.getIdentifier());
            }

            // Default port number test.
            if (order != null) {
                VtnSetPortSrcAction vact = new VtnSetPortSrcActionBuilder().
                    build();
                VtnSetPortSrcActionCase vac = vacBuilder.
                    setVtnSetPortSrcAction(vact).build();
                VTNSetPortSrcAction va = new VTNSetPortSrcAction(vac, order);
                assertEquals(order, va.getIdentifier());
                assertEquals(0, va.getPort());

                vac = vacBuilder.setVtnSetPortSrcAction(null).build();
                va = new VTNSetPortSrcAction(vac, order);
                assertEquals(order, va.getIdentifier());
                assertEquals(0, va.getPort());
            }
        }

        VTNSetPortSrcAction va = new VTNSetPortSrcAction();
        for (int port: ports) {
            // toFlowAction() test.
            PortNumber pnum = new PortNumber(port);
            vad = new org.opendaylight.vtn.manager.flow.action.
                SetTpSrcAction(port);
            VtnSetPortSrcAction vact = new VtnSetPortSrcActionBuilder().
                setPort(pnum).build();
            VtnAction vaction = vacBuilder.
                setVtnSetPortSrcAction(vact).build();
            assertEquals(vad, va.toFlowAction(vaction));

            vaction = VTNSetPortDstAction.newVtnAction(pnum);
            RpcErrorTag etag = RpcErrorTag.BAD_ELEMENT;
            StatusCode ecode = StatusCode.BADREQUEST;
            String emsg = "VTNSetPortSrcAction: Unexpected type: " + vaction;
            try {
                va.toFlowAction(vaction);
                unexpected();
            } catch (RpcException e) {
                assertEquals(etag, e.getErrorTag());
                Status st = e.getStatus();
                assertEquals(ecode, st.getCode());
                assertEquals(emsg, st.getDescription());
            }

            // toFlowAction() should never affect instance variables.
            assertEquals(0, va.getPort());

            // toVtnAction() test.
            SetTpSrcAction ma = new SetTpSrcActionBuilder().
                setPort(pnum).build();
            Action action = new SetTpSrcActionCaseBuilder().
                setSetTpSrcAction(ma).build();
            vaction = vacBuilder.setVtnSetPortSrcAction(vact).build();
            assertEquals(vaction, va.toVtnAction(action));

            action = new SetTpDstActionCaseBuilder().build();
            emsg = "VTNSetPortSrcAction: Unexpected type: " + action;
            try {
                va.toVtnAction(action);
                unexpected();
            } catch (RpcException e) {
                assertEquals(etag, e.getErrorTag());
                Status st = e.getStatus();
                assertEquals(ecode, st.getCode());
                assertEquals(emsg, st.getDescription());
            }

            try {
                va.getDescription(action);
                unexpected();
            } catch (RpcException e) {
                assertEquals(etag, e.getErrorTag());
                Status st = e.getStatus();
                assertEquals(ecode, st.getCode());
                assertEquals(emsg, st.getDescription());
            }

            etag = RpcErrorTag.MISSING_ELEMENT;
            action = new SetTpSrcActionCaseBuilder().build();
            emsg = "VTNSetPortSrcAction: No port number: " + action;
            try {
                va.toVtnAction(action);
                unexpected();
            } catch (RpcException e) {
                assertEquals(etag, e.getErrorTag());
                Status st = e.getStatus();
                assertEquals(ecode, st.getCode());
                assertEquals(emsg, st.getDescription());
            }

            action = new SetTpSrcActionCaseBuilder().
                setSetTpSrcAction(new SetTpSrcActionBuilder().build()).
                build();
            emsg = "VTNSetPortSrcAction: No port number: " + action;
            try {
                va.toVtnAction(action);
                unexpected();
            } catch (RpcException e) {
                assertEquals(etag, e.getErrorTag());
                Status st = e.getStatus();
                assertEquals(ecode, st.getCode());
                assertEquals(emsg, st.getDescription());
            }

            // toVtnAction() should never affect instance variables.
            assertEquals(0, va.getPort());

            // getDescription() test.
            action = new SetTpSrcActionCaseBuilder().
                setSetTpSrcAction(ma).build();
            String desc = "SET_TP_SRC(port=" + port + ")";
            assertEquals(desc, va.getDescription(action));

            // getDescription() should never affect instance variables.
            assertEquals(0, va.getPort());
        }

        Action action = new SetTpSrcActionCaseBuilder().build();
        String desc = "SET_TP_SRC(port=null)";
        assertEquals(desc, va.getDescription(action));
        assertEquals(0, va.getPort());

        action = new SetTpSrcActionCaseBuilder().
            setSetTpSrcAction(new SetTpSrcActionBuilder().build()).
            build();
        assertEquals(desc, va.getDescription(action));
        assertEquals(0, va.getPort());

        // Null order.
        RpcErrorTag etag = RpcErrorTag.MISSING_ELEMENT;
        StatusCode ecode = StatusCode.BADREQUEST;
        PortNumber pnum = new PortNumber(1);
        String emsg = "VTNSetPortSrcAction: Action order cannot be null";
        VtnSetPortSrcAction vact = new VtnSetPortSrcActionBuilder().
            setPort(pnum).build();
        VtnSetPortSrcActionCase vac = vacBuilder.
            setVtnSetPortSrcAction(vact).build();
        try {
            new VTNSetPortSrcAction(vac, (Integer)null);
            unexpected();
        } catch (RpcException e) {
            assertEquals(etag, e.getErrorTag());
            Status st = e.getStatus();
            assertEquals(ecode, st.getCode());
            assertEquals(emsg, st.getDescription());
        }

        // Invalid port number.
        etag = RpcErrorTag.BAD_ELEMENT;
        int[] invalid = {
            Integer.MIN_VALUE, -0xfff0000, -1000000, -33333, -2, -1,
            65536, 65537, 0xabcdef, 0xfff00000, Integer.MAX_VALUE,
        };
        for (int port: invalid) {
            emsg = "VTNSetPortSrcAction: Invalid port number: " + port;
            vad = new org.opendaylight.vtn.manager.flow.action.
                SetTpSrcAction(port);
            try {
                new VTNSetPortSrcAction(vad, 1);
                unexpected();
            } catch (RpcException e) {
                assertEquals(etag, e.getErrorTag());
                Status st = e.getStatus();
                assertEquals(ecode, st.getCode());
                assertEquals(emsg, st.getDescription());
            }
        }

        // Default value test for toFlowAction().
        vad = new org.opendaylight.vtn.manager.flow.action.SetTpSrcAction(0);
        vac = vacBuilder.setVtnSetPortSrcAction(null).build();
        assertEquals(vad, va.toFlowAction(vac));

        vac = vacBuilder.
            setVtnSetPortSrcAction(new VtnSetPortSrcActionBuilder().build()).
            build();
        assertEquals(vad, va.toFlowAction(vac));
    }

    /**
     * Test case for {@link VTNSetPortSrcAction#newVtnAction(PortNumber)}.
     */
    @Test
    public void testNewVtnAction() {
        PortNumber[] values = {
            null,
            new PortNumber(0),
            new PortNumber(1),
            new PortNumber(9999),
            new PortNumber(32767),
            new PortNumber(32768),
            new PortNumber(65534),
            new PortNumber(65535),
        };

        for (PortNumber pnum: values) {
            VtnSetPortSrcActionCase ac =
                VTNSetPortSrcAction.newVtnAction(pnum);
            VtnSetPortSrcAction vaction = ac.getVtnSetPortSrcAction();
            assertEquals(pnum, vaction.getPort());
        }
    }

    /**
     * Test case for the following methods.
     *
     * <ul>
     *   <li>{@link VTNSetPortSrcAction#equals(Object)}</li>
     *   <li>{@link VTNSetPortSrcAction#hashCode()}</li>
     *   <li>{@link VTNSetPortDstAction#equals(Object)}</li>
     *   <li>{@link VTNSetPortDstAction#hashCode()}</li>
     * </ul>
     *
     * @throws Exception  An error occurred.
     */
    @Test
    public void testEquals() throws Exception {
        HashSet<Object> set = new HashSet<>();
        int[] ports = {
            0, 1, 10, 33, 1234, 32767, 32768, 40000, 55555, 65535,
        };
        Integer[] orders = {
            Integer.MIN_VALUE, -1000, -1,
            0, 1, 2, 32000, Integer.MAX_VALUE,
        };

        for (int port: ports) {
            VTNSetPortSrcAction vsrc1 = new VTNSetPortSrcAction(port);
            VTNSetPortSrcAction vsrc2 = new VTNSetPortSrcAction(port);
            VTNSetPortDstAction vdst1 = new VTNSetPortDstAction(port);
            VTNSetPortDstAction vdst2 = new VTNSetPortDstAction(port);

            // VTNSetPortSrcAction should be distinguished from
            // VTNSetPortDstAction.
            testEquals(set, vsrc1, vsrc2);
            testEquals(set, vdst1, vdst2);

            for (Integer order: orders) {
                VtnSetPortSrcActionCase src1 = VTNSetPortSrcAction.
                    newVtnAction(new PortNumber(port));
                VtnSetPortSrcActionCase src2 = VTNSetPortSrcAction.
                    newVtnAction(new PortNumber(port));
                vsrc1 = new VTNSetPortSrcAction(src1, order);
                vsrc2 = new VTNSetPortSrcAction(src2, order);

                VtnSetPortDstActionCase dst1 = VTNSetPortDstAction.
                    newVtnAction(new PortNumber(port));
                VtnSetPortDstActionCase dst2 = VTNSetPortDstAction.
                    newVtnAction(new PortNumber(port));
                vdst1 = new VTNSetPortDstAction(dst1, order);
                vdst2 = new VTNSetPortDstAction(dst2, order);

                // VTNSetPortSrcAction should be distinguished from
                // VTNSetPortDstAction.
                testEquals(set, vsrc1, vsrc2);
                testEquals(set, vdst1, vdst2);
            }
        }

        int expected = (orders.length + 1) * ports.length * 2;
        assertEquals(expected, set.size());
    }

    /**
     * Test case for {@link VTNSetPortSrcAction#toString()}.
     *
     * @throws Exception  An error occurred.
     */
    @Test
    public void testToString() throws Exception {
        int[] ports = {
            0, 1, 10, 33, 1234, 32767, 32768, 40000, 55555, 65535,
        };
        Integer [] orders = {
            null, Integer.MIN_VALUE, -1000, -1,
            0, 1, 2, 32000, Integer.MAX_VALUE,
        };

        for (Integer order: orders) {
            for (int port: ports) {
                VTNSetPortSrcAction va;
                String expected;
                if (order == null) {
                    va = new VTNSetPortSrcAction(port);
                    expected = "VTNSetPortSrcAction[port=" + port + "]";
                } else {
                    VtnSetPortSrcActionCase vac = VTNSetPortSrcAction.
                        newVtnAction(new PortNumber(port));
                    va = new VTNSetPortSrcAction(vac, order);
                    expected = "VTNSetPortSrcAction[port=" + port +
                        ",order=" + order + "]";
                }
                assertEquals(expected, va.toString());
            }
        }
    }

    /**
     * Test case for {@link VTNSetPortSrcAction#apply(FlowActionContext)}.
     *
     * @throws Exception  An error occurred.
     */
    @Test
    public void testApply() throws Exception {
        Integer order = 10;
        int port = 33333;
        VtnSetPortSrcActionCase vac = VTNSetPortSrcAction.
            newVtnAction(new PortNumber(port));
        VTNSetPortSrcAction va = new VTNSetPortSrcAction(vac, order);

        // In case of TCP packet.
        FlowActionContext ctx = Mockito.mock(FlowActionContext.class);
        TcpHeader tcp = Mockito.mock(TcpHeader.class);
        Mockito.when(ctx.getLayer4Header()).thenReturn(tcp);

        assertEquals(true, va.apply(ctx));
        Mockito.verify(ctx).getLayer4Header();
        Mockito.verify(ctx).addFilterAction(va);
        Mockito.verify(ctx, Mockito.never()).
            removeFilterAction(Mockito.any(Class.class));
        Mockito.verify(ctx, Mockito.never()).getFilterAction();

        Mockito.verify(tcp).setSourcePort(port);
        Mockito.verify(tcp, Mockito.never()).getSourcePort();
        Mockito.verify(tcp, Mockito.never()).getDestinationPort();
        Mockito.verify(tcp, Mockito.never()).
            setDestinationPort(Mockito.anyInt());

        // In case of UDP packet.
        Mockito.reset(ctx);
        UdpHeader udp = Mockito.mock(UdpHeader.class);
        Mockito.when(ctx.getLayer4Header()).thenReturn(udp);

        assertEquals(true, va.apply(ctx));
        Mockito.verify(ctx).getLayer4Header();
        Mockito.verify(ctx).addFilterAction(va);
        Mockito.verify(ctx, Mockito.never()).
            removeFilterAction(Mockito.any(Class.class));
        Mockito.verify(ctx, Mockito.never()).getFilterAction();

        Mockito.verify(udp).setSourcePort(port);
        Mockito.verify(udp, Mockito.never()).getSourcePort();
        Mockito.verify(udp, Mockito.never()).getDestinationPort();
        Mockito.verify(udp, Mockito.never()).
            setDestinationPort(Mockito.anyInt());

        // In case of ICMP packet.
        Mockito.reset(ctx);
        IcmpHeader icmp = Mockito.mock(IcmpHeader.class);
        Mockito.when(ctx.getLayer4Header()).thenReturn(icmp);

        assertEquals(false, va.apply(ctx));
        Mockito.verify(ctx).getLayer4Header();
        Mockito.verify(ctx, Mockito.never()).
            addFilterAction(Mockito.any(FlowFilterAction.class));
        Mockito.verify(ctx, Mockito.never()).
            removeFilterAction(Mockito.any(Class.class));
        Mockito.verify(ctx, Mockito.never()).getFilterAction();

        Mockito.verifyZeroInteractions(icmp);

        // In case of non-L4 packet.
        Mockito.reset(ctx);
        Mockito.when(ctx.getLayer4Header()).thenReturn(null);

        assertEquals(false, va.apply(ctx));
        Mockito.verify(ctx).getLayer4Header();
        Mockito.verify(ctx, Mockito.never()).
            addFilterAction(Mockito.any(FlowFilterAction.class));
        Mockito.verify(ctx, Mockito.never()).
            removeFilterAction(Mockito.any(Class.class));
        Mockito.verify(ctx, Mockito.never()).getFilterAction();
    }

    /**
     * Ensure that {@link VTNSetPortSrcAction} is mapped to XML root element.
     *
     * @throws Exception  An error occurred.
     */
    @Test
    public void testJAXB() throws Exception {
        Unmarshaller[] unmarshallers = {
            createUnmarshaller(VTNSetPortSrcAction.class),
            createUnmarshaller(VTNPortAction.class),
            createUnmarshaller(FlowFilterAction.class),
        };

        int[] ports = {
            0, 1, 10, 33, 1234, 32767, 32768, 40000, 55555, 65535,
        };
        int[] orders = {
            Integer.MIN_VALUE, -1000, -1,
            0, 1, 2, 32000, Integer.MAX_VALUE,
        };

        Class<VTNSetPortSrcAction> type = VTNSetPortSrcAction.class;
        List<XmlDataType> dlist = getXmlDataTypes(XML_ROOT);
        for (Unmarshaller um: unmarshallers) {
            for (Integer order: orders) {
                for (int port: ports) {
                    String xml = new XmlNode(XML_ROOT).
                        add(new XmlNode("order", order)).
                        add(new XmlNode("port", port)).toString();
                    VTNSetPortSrcAction va = unmarshal(um, xml, type);
                    va.verify();
                    assertEquals(order, va.getIdentifier());
                    assertEquals(port, va.getPort());
                }

                // Default port number test.
                String xml = new XmlNode(XML_ROOT).
                    add(new XmlNode("order", order)).toString();
                VTNSetPortSrcAction va = unmarshal(um, xml, type);
                va.verify();
                assertEquals(order, va.getIdentifier());
                assertEquals(0, va.getPort());
            }

            // Ensure that broken values in XML can be detected.
            jaxbErrorTest(um, type, dlist);
        }

        // No action order.
        Unmarshaller um = unmarshallers[0];
        RpcErrorTag etag = RpcErrorTag.MISSING_ELEMENT;
        StatusCode ecode = StatusCode.BADREQUEST;
        String emsg = "VTNSetPortSrcAction: Action order cannot be null";
        String xml = new XmlNode(XML_ROOT).
            add(new XmlNode("port", 12345)).toString();
        VTNSetPortSrcAction va = unmarshal(um, xml, type);
        try {
            va.verify();
            unexpected();
        } catch (RpcException e) {
            assertEquals(etag, e.getErrorTag());
            Status st = e.getStatus();
            assertEquals(ecode, st.getCode());
            assertEquals(emsg, st.getDescription());
        }

        // Invalid port number.
        etag = RpcErrorTag.BAD_ELEMENT;
        int[] invalid = {
            Integer.MIN_VALUE, -0xfff0000, -1000000, -33333, -2, -1,
            65536, 65537, 0xabcdef, 0xfff00000, Integer.MAX_VALUE,
        };
        for (int port: invalid) {
            emsg = "VTNSetPortSrcAction: Invalid port number: " + port;
            xml = new XmlNode(XML_ROOT).
                add(new XmlNode("order", 1)).
                add(new XmlNode("port", port)).toString();
            va = unmarshal(um, xml, type);
            try {
                va.verify();
                unexpected();
            } catch (RpcException e) {
                assertEquals(etag, e.getErrorTag());
                Status st = e.getStatus();
                assertEquals(ecode, st.getCode());
                assertEquals(emsg, st.getDescription());
            }
        }
    }
}