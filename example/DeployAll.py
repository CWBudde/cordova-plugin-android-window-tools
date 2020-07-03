import subprocess;
import sys
import time

devices = subprocess.run('adb devices -l', capture_output=True).stdout.splitlines();
devices.remove(devices[0]);
devices.remove(devices[len(devices) - 1]);

calls = [
    'adb -s %s shell input keyevent 82',
];

if len(sys.argv) >= 2:
    calls.append('adb -s %s shell am start -a android.intent.action.MAIN -n' + sys.argv[1] + '/.MainActivity')

if len(sys.argv) == 3:
    calls.insert(0, 'adb -s %s install -r ' + sys.argv[2])

class SubProc():
    index = 0

    def __init__(self,deviceId):
        self.index = SubProc.index
        self.deviceId = deviceId
        self.callIndex = 0;
        SubProc.index += 1
        self.nextCall()

    def nextCall(self):
        if self.callIndex < len(calls):
            self.proc = subprocess.Popen(calls[self.callIndex] % self.deviceId)
            self.callIndex += 1
            return False
        return True
        
        
running_procs = [];
for device in devices:
    elements = device.decode().split(' ');
    deviceId = elements[0];

    model = deviceId;
    for element in elements:
        if element.startswith('model:'):
            model = element.replace('model:', '') 
            break;

    print('Found Device: ' + model);    
    running_procs.append(SubProc(deviceId));

while running_procs:
    for proc in running_procs:
        retcode = proc.proc.poll()
        if retcode is not None: # Process finished.
            if proc.nextCall() == True:
                running_procs.remove(proc)
            break
        else: # No process is done, wait a bit and check again.
            time.sleep(.1)
            continue

    # Here, `proc` has finished with return code `retcode`
    if retcode != None and retcode != 0:
        """Error handling."""
    #handle_results(proc.stdout)