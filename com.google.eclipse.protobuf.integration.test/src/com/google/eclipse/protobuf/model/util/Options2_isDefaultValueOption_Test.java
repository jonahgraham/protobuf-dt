/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.model.util;

import static com.google.eclipse.protobuf.junit.core.Setups.integrationTestSetup;
import static com.google.eclipse.protobuf.junit.core.XtextRule.createWith;
import static org.junit.Assert.*;

import org.junit.*;

import com.google.eclipse.protobuf.junit.core.XtextRule;
import com.google.eclipse.protobuf.protobuf.FieldOption;

/**
 * Tests for <code>{@link Options2#isDefaultValueOption(FieldOption)}</code>.
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class Options2_isDefaultValueOption_Test {

  @Rule public XtextRule xtext = createWith(integrationTestSetup());

  private Options2 options;

  @Before public void setUp() {
    options = xtext.getInstanceOf(Options2.class);
  }

  // syntax = "proto2";
  //
  // message Person {
  //   optional boolean active = 1 [default = true, deprecated = false];
  // }
  @Test public void should_return_true_if_FieldOption_is_default_value_one() {
    FieldOption option = xtext.find("default", FieldOption.class);
    assertTrue(options.isDefaultValueOption(option));
  }

  // syntax = "proto2";
  //
  // message Person {
  //   optional boolean active = 1 [default = true, deprecated = false];
  // }
  @Test public void should_return_false_if_FieldOption_is_not_default_value_one() {
    FieldOption option = xtext.find("deprecated", FieldOption.class);
    assertFalse(options.isDefaultValueOption(option));
  }
}