package client;

import com.apple.dnssd.*;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class MenuBarIconTest {
	public static void main(String[] args) throws MalformedURLException, DNSSDException {
		DNSSDRegistration r = DNSSD.register("Trevor", "_talkie._udp", 6882, new RegisterListener() {

			@Override
			public void operationFailed(DNSSDService arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void serviceRegistered(DNSSDRegistration arg0, int arg1,
					String arg2, String arg3, String arg4) {
				
				System.out.println("Sweet");
			}
			
		});
		
		
		DNSSDService b = DNSSD.browse("_ssh._tcp", new BrowseListener(){
			@Override
			public void operationFailed(DNSSDService svc, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void serviceFound(DNSSDService svc, int arg1, int arg2,
					String arg3, String arg4, String arg5) {
				
				System.out.println(arg3);
			}

			@Override
			public void serviceLost(DNSSDService svc, int arg1, int arg2,
					String arg3, String arg4, String arg5) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	    TrayIcon trayIcon = null;
	     if (SystemTray.isSupported()) {
	         // get the SystemTray instance
	         SystemTray tray = SystemTray.getSystemTray();
	         // load an image
	         Image image = Toolkit.getDefaultToolkit().getImage(new URL("http://cdn1.iconfinder.com/data/icons/Hypic_Icon_Pack_by_shlyapnikova/16/forum_16.png"));
	         // create a action listener to listen for default action executed on the tray icon
	         ActionListener listener = new ActionListener() {
	             public void actionPerformed(ActionEvent e) {
	            	 System.out.println("action");
	                 // execute default action of the application
	                 // ...
	             }
	         };
	         // create a popup menu
	         PopupMenu popup = new PopupMenu();
	         // create menu item for the default action
	         MenuItem defaultItem = new MenuItem("Do the action");
	         defaultItem.addActionListener(listener);
	         popup.add(defaultItem);
	         /// ... add other items
	         // construct a TrayIcon
	         trayIcon = new TrayIcon(image, "Tray Demo", popup);
	         // set the TrayIcon properties
	         trayIcon.addActionListener(listener);
	         // ...
	         // add the tray image
	         try {
	             tray.add(trayIcon);
	         } catch (AWTException e) {
	             System.err.println(e);
	         }
	         // ...
	     } else {
	         // disable tray option in your application or
	         // perform other actions
	         //...
	     }
	     // ...
	     // some time later
	     // the application state has changed - update the image
	     if (trayIcon != null) {
	         //trayIcon.setImage(updatedImage);
	     }

	}
}
