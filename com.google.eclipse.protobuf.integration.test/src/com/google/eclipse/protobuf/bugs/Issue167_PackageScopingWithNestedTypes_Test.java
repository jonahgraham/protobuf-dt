/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.bugs;

import static com.google.eclipse.protobuf.junit.IEObjectDescriptions.descriptionsIn;
import static com.google.eclipse.protobuf.junit.core.Setups.integrationTestSetup;
import static com.google.eclipse.protobuf.junit.core.XtextRule.createWith;
import static com.google.eclipse.protobuf.junit.matchers.ContainNames.contain;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import com.google.eclipse.protobuf.junit.core.XtextRule;
import com.google.eclipse.protobuf.protobuf.*;
import com.google.eclipse.protobuf.scoping.ProtobufScopeProvider;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.scoping.IScope;
import org.junit.*;

/**
 * Tests fix for <a href="http://code.google.com/p/protobuf-dt/issues/detail?id=167">Issue 167</a>.
 * 
 * @author alruiz@google.com (Alex Ruiz)
 */
public class Issue167_PackageScopingWithNestedTypes_Test {

  private static EReference reference;

  @BeforeClass public static void setUpOnce() {
    reference = mock(EReference.class);
  }

  @Rule public XtextRule xtext = createWith(integrationTestSetup());

  private ProtobufScopeProvider provider;

  @Before public void setUp() {
    provider = xtext.getInstanceOf(ProtobufScopeProvider.class);
  }

  // // Create file types.proto
  //
  // syntax = 'proto2';
  // package com.google.proto.base.shared;
  // 
  // message Outer {
  //   enum Type {
  //     ONE = 1;
  //     TWO = 2;
  //   }
  // }
  
  // syntax = "proto2";
  // package com.google.proto.project.shared;
  //
  // import "types.proto";
  //
  // message Summary {
  //   repeated base.shared.Outer.Type type = 1;
  // }
  @Test public void should_include_package_intersection() {
    MessageField field = xtext.find("type", " =", MessageField.class);
    IScope scope = provider.scope_ComplexTypeLink_target((ComplexTypeLink) field.getType(), reference);
    assertThat(descriptionsIn(scope), contain("base.shared.Outer.Type", "proto.base.shared.Outer.Type", 
                                              "google.proto.base.shared.Outer.Type", 
                                              "com.google.proto.base.shared.Outer.Type"));
  }
}
