# Lab: Bluetooth

For this lab, you learn about some of the pieces of creating a connection between two co-located devices using [Bluetooth](https://en.wikipedia.org/wiki/Bluetooth).

Android provides a detailed [API][http://developer.android.com/guide/topics/connectivity/bluetooth.html] (open this link for reference!) for interactive with Bluetooth. There are a number of steps and details, so you'll be working with a project that has many of the fussy pieces in place for you (like reading and writing data over a Socket!).

- You'll be filling the the remaining pieces from a Sample Project provided by Google. Google includes lots of samples demonstrating how to use particular pieces of functionality; reading and adapting the provided projects is a great way to learn new skills. There are _a lot_ of comments, that that sometimes makes it hard to follow all the pieces. Read carefully!

- A lot of this work will actually just be using Intents to interact with the Bluetooth radio, so you may want your notes for that handy.

Open up this project in Android Studio to get started. **IMPORTANT NOTE**: you will need to run this project on a physical device, not the emulator---the emulator doesn't support Bluetooth :(

## Your Tasks:
Your task is to fill in the missing pieces of code, following the instructions below. I've marked each location with a `TODO` comment, which should show up in blue in Android Studio.

1. Start by reading through [The Basics](http://developer.android.com/guide/topics/connectivity/bluetooth.html#TheBasics) to get a sense for what classes will be in place and what their roles are. You only need to focus on the first 4: `BluetoothAdapter`, `BluetoothDevice`, `BluetoothSocket`, and `BluetoothServerSocket` (the rest are for other kinds of Bluetooth connections, like audio transfer and stuff). You don't need to know all the methods or details of these classes, but should be familiar with their general, one-sentence purpose!

2. You'll need to request [permission][http://developer.android.com/guide/topics/connectivity/bluetooth.html#Permissions] to use Bluetooth. Add the appropriate `<uses-permission>` attributes: one for `BLUETOOTH` (for communication) and one for `BLUETOOTH_ADMIN` (to "discover" devices and make connections).

3. The main UI is defined in the `BluetoothChatFragment` class, which is a Fragment that holds the chat system. Start by filling in the `onCreate()` callback by <a href="http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#getDefaultAdapter()">fetching the default Bluetooth adapter</a> and saving it as an instance variable (`mBluetoothAdapter`). If the adapter doesn't exist (is `null`), you should `Toast` a message that Bluetooth isn't available, and then call `finish()` on the

4. You'll want your app to make sure that the user has Bluetooth turned on. In the `onStart()` method (assuming the adapter exists), check to see if the `BluetoothAdapter` (the one you just fetched) `isEnabled()`.

  - If it **IS NOT** enabled, you'll want to prompt the user to enable it, such as by launching the "Settings" app. Create an **Implicit Intent** for the action `BluetoothAdapter.ACTION_REQUEST_ENABLED`, and send this intent for a _result_ (with the result code of `REQUEST_ENABLE_BT`). Check in the `onActivityResult()` method to see what happens when we get a response back!

  - If the adapter **IS** enabled, you'll also want to check that the `BluetoothChatService` (stored in the instance variable `mChatService`) is `null`--and if it is, then call the `setupChat()` helper method to create it.

5. In order for a device to connect to yours over Bluetooth, your device will need to be **discoverable**: effectively, it has to respond to public queries about its existence (sort of like having your instant messaging status as "Online/Available"). In the `ensureDiscoverable()` helper method, check if the device is currently discoverable by calling `getScanMode()` on the `BluetoothAdapter`---it should return a value of `BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE`.

  - If this **IS NOT** the case, then you should send another _Implicit Intent_ to handle the `BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE` action.

    - You should also include (put) an **extra** that has the key `BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION` and a value of `300`, so that we are in "discoverable" mode for 300 seconds.

    - This intent does **NOT** need to be started for a result!

6. The discovery of devices is controlled by the `DeviceListActivity` Activity. This is a separate Activity that will actually appear as a popup dialog (though it doesn't use `DialogFragment`; it just "themes" the Activity as a dialog in the `Manifest`). The Activity's `onCreate()` does a lot of UI work (including setting up an Adapter!), but it also needs to set up a `BroadcastReceiver` to listen for events like when devices are found.

  - This is the equivalent of declaring a `<receiver>` and `<intent-filter>` in the `Manifest`, but we need to do it in Java since the Receiver isn't a separate class.

  - First instantiate a new <a href="http://developer.android.com/reference/android/content/IntentFilter.html#IntentFilter(java.lang.String)">`IntentFilter`</a> object (giving it the `BluetoothDevice.ACTION_FOUND` action).

  - Then use the <a href="http://developer.android.com/reference/android/content/Context.html#registerReceiver(android.content.BroadcastReceiver, android.content.IntentFilter)">`registReceiver(receiver, intentFilter)`</a> method, passing it the receiver (`mReceiver`) and the `IntentFilter` you just created!

  - Repeat the above two steps, but for the `Bluetooth.ACTION_DISCOVERY_FINISHED` action.

7. We can actually begin searching for devices by filling in the `doDiscover()` helper method (called when the Scan button is pressed).

  - Add a check to see if the `BluetoothAdapter` currently `isDiscovering()`. If so, then you should tell the adapter to `cancelDiscovery()`.

  - No matter if the check is true or false, tell the adapter to `startDiscovery()` to begin searching for devices!

8. Once the user has selected a device to connect to, we handle that connection back in the `BluetoothChatFragment`, in the `connectDevice()` helper method. Fill in this method to connect to the device!

  - First you'll want to get the device's "address" (a [MAC address](https://en.wikipedia.org/wiki/MAC_address) that acts as a unique identifier) _from_ the Intent's extras: get the `Bundle` of extras from the Intent, then get the String with the key `DeviceListActivity.EXTRA_DEVICE_ADDRESS`.

  - You can then find the device (a `BluetoothDevice` object) by calling the `.getRemoteDevice()` method on the `BluetoothAdapter` and passing this address

  - Finally, you can use the `mChatService`'s `.connect()` method to connect to this device (passing down the `secure` option as a second parameter). The `BluetoothChatService.connect()` method creates a new Thread to do the communication work, and opens up network sockets so that messages can be passed between the devices. (This is actually part of the hard part or working with Bluetooth; luckily we have a class to abstract that for us!)

9. The last part is to actually send a message! In the `sendMessage()` helper in `BluetoothChatFragment`, fill in the details so that the String can be send to the socket in the chat service.

  - First you need to convert the message String into a `byte[]` (for communication over the socket). Use the String's `getBytes()` method to convert.

  - Then you can tell `mChatService` to `.write()` those bytes!

  - We then need to reset the `mOutStringBuffer` instance variable (which keeps track of the message that has been typed so far). Use `.setLength()` to give it a length of **0**, which will effectively make it empty.

  - And finally, because we've changed the outgoing message, set the text of the `mOutEditText` TextView to be the (now empty) `mOutStringBuffer`.

And that's it! You should now have a working chat system! Search for and connect to someone else's device and try saying "hello"! :)
