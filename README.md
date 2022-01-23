# trans_ip
Application allowing the transfer of files between two computers on a local network.

>Watch the demonstration on youTube: [trans_ip](https://youtu.be/7Lsckb7YThk)

# libs

For the functioning of the application, it requires that some libraries are integrated in the project. So, you have to integrate the libraries that are in the `/libs` folder in your project.
Note that this project is designed with `JDK 1.8`.

# How it works

`You have to launch the receiving server first`

We do this by clicking on the receive tab (`Recevoir`), then we define a port ( `port: 5745` by default ).
Finally, we choose the folder where we want to save our files and click on Download (`Télécharger`).

![](/imgs/receive.png)

`Select file to send`

To send a file in our local network, we have to click on the send tab (`Envoyer`), then we enter the same port as the server's ( `port:5745` by default). Then we configure the IP address of the computer in the network (`IP address: 127.0.0.1` by default). Then we select the file to transmit and we click on send (`Envoyer`).

![](/imgs/send.png)