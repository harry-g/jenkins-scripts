#!/bin/bash
time du -ak /net/fe0vmc0*/fs0/jenkins | gzip > du.log-JenkinsHome-$( date +%Y%m%d-%H%M%S ).gz
