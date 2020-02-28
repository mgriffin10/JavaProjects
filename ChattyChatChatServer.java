import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class ChattyChatChatServer
	{
		//create static arrays of sockets and usernames
		public static ArrayList<Socket> users = new ArrayList<Socket>();
		public static ArrayList<String> usernames = new ArrayList<String>();
		//create static variable port and set to 0
		public static int port = 0;

		// main takes port number as parameter and sets up server
		public static void main (String[] args) throws Exception
		{
			// set port to int passed on command line
			setPort(Integer.parseInt(args[0]));
			// create instance of ServerSocket and set it to port number
			ServerSocket server = new ServerSocket(getPort());

			// Success print statement, exception thrown if socket fails
			System.out.println("The server is open and running.");

			//While loop runs while server is open accepts new clients, creates instance of protocol
			// and begins threads for each client
			while (true)
			{
				try
				{
					//accept new clients
					Socket socket = server.accept();
					//add socket and filler nickname to arrays
					users.add(socket);
					usernames.add("NewUser");
					//create instance of Protocol class passing the new socket
					Protocol chatter = new Protocol(socket);
					//create thread with instance of protocol
					Thread chatting = new Thread(chatter);
					//begin the thread
					chatting.start();
				} //end try block

				catch (IOException e)
				{

				} //end catch IO Exc/queption
			} //end of while loop
		}//end function main

		////////////////////////////////////
		//Port getter and setter functions//
		////////////////////////////////////
		public static void setPort(int inputPort) { port = inputPort; }
		public static int getPort() {
			return port;
		}

		//function control directs the messages and nick name assignment of server
		public void Control(int port, String fullmessage)
		{
			try
			{
				String[] messageArray;
				String delim = " ";
				messageArray = fullmessage.split(delim, 4);
				//split the message and set it into the messageArray using the space delimiters.

//				System.out.println(messageArray[0]);
				System.out.println(messageArray[0]);
				System.out.println(messageArray[1]);
//				System.out.println(messageArray[2]);
//				System.out.println(messageArray[3]);


				for (int x = 0; x < users.size(); x++)
				{
					//sets the nickname if the user creates it
					if (messageArray[1].equals("nick") && (users.get(x).getPort() == port))
					{
						System.out.println("Entering nickname if.");

						usernames.set(x, messageArray[2]);
					}

					// DM
					else if (messageArray[1].equals("/dm"))
					{
						System.out.println("Entering DM else if.");

						System.out.println(messageArray[0]);
						System.out.println(messageArray[1]);
						System.out.println(messageArray[2]);
						System.out.println(messageArray[3]);

						//if there is a username that matches the input in the messageArray,
						//send that message to a user.
						if (usernames.get(x).equals(messageArray[2]))
						{
							PrintWriter writer = new PrintWriter(users.get(x).getOutputStream());
							writer.println(messageArray[3]);
							writer.flush();

						}
						//else username is not a match the message is ignored

					}

					// Mass Message
					else
					{
						PrintWriter writer = new PrintWriter(users.get(x).getOutputStream());
						writer.println(fullmessage);
						writer.flush();
					}

				} //end for loop
			} //end try block

			catch (IOException e)
			{

			} //end catch IO Exception
		} //end function Control
	  ///////////////////////////////////////
	} //end of Class ChattyChatChatServer  //
      ///////////////////////////////////////

//Protocol class that runs the server end of nickname setting and reads the input messages from Clients
class Protocol implements Runnable
	{
		private Socket socket;
		
		//no-purpose constructor
		public Protocol()
		{
		}

		//constructor that takes a socket as a value
		public Protocol(Socket inputSocket)
		{
			socket = inputSocket;
		}

		public void run()
		{
			try
			{
				Scanner scanner = new Scanner(socket.getInputStream());
				ChattyChatChatServer outputServer = new ChattyChatChatServer();

				while (true)
				{
					if (scanner.hasNext())
					{
						//String delimiter = " ";
						//String[] inputArray;

						String fullmessage = scanner.nextLine();

						//String nickname = null;
						//inputArray = message.split(delimiter, 3);

//						//sets the nickname if the user creates it
//						if (inputArray[0].equals("/nick"))
//						{
//							nickname = inputArray[1];
//						}

						// calling Control function, passing port, null or nickname, and the message
						outputServer.Control(socket.getPort(), fullmessage);
					}
					
				} //end of while loop
			} //end try block

			catch (IOException e)
			{
				
			} //end catch IO Exception
		} //end function run
      //////////////////////////
	} //end of Class Protocol //
      //////////////////////////
	

