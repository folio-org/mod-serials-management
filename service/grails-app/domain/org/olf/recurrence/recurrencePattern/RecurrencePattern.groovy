package org.olf

import grails.gorm.MultiTenant

public class RecurrencePattern implements MultiTenant<RecurrencePattern> {
  String id
  RecurrenceRule owner
}