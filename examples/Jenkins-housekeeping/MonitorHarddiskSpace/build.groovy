def fp = jenkins.model.Jenkins.instance.getRootPath()
def ts = fp.getTotalDiskSpace()
def us = fp.getUsableDiskSpace()

def ratio = us/ts

// set to unstable if left spacce < 10%
if (ratio < 0.1) 
{
  Thread.currentThread().executable.result = hudson.model.Result.UNSTABLE
}
