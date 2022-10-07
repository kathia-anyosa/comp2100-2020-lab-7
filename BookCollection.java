import com.google.gson.internal.GsonBuildConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.xml.sax.SAXException;

// you need to import some xml libraries

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// import any standard library if needed

/**
 * A book collection holds 0 or more books in a collection.
 */
public class BookCollection {
	private List<Book> books;

	/**
	 * Creates a new collection with no books by default.
	 */
	public BookCollection() {
		this.books = new ArrayList<Book>();
	}

	/**
	 * Creates a new book collection with the specified list of books pre-defined.
	 *
	 * @param books A books list.
	 */
	public BookCollection(List<Book> books) {
		this.books = books;
	}

	/**
	 * Returns the current list of books stored by this collection.
	 *
	 * @return A (mutable) list of books.
	 */
	public List<Book> getList() {
		return books;
	}

	/**
	 * Sets the list of books in this collection to the specified value.
	 */
	public void setList(List<Book> books) {
		this.books = books;
	}

	/**
	 * A simple human-readable toString implementation. Not particularly useful to
	 * save to disk.
	 *
	 * @return A human-readable string for printing
	 */
	@Override
	public String toString() {
		return this.books.stream().map(book -> " - " + book.display() + "\n").collect(Collectors.joining());
	}

	/**
	 * Saves this collection to the specified "bespoke" file.
	 *
	 * @param file The path to a file.
	 */
	public void saveToBespokeFile(File file) throws IOException {
		// TODO: Implement this function yourself. The specific hierarchy is up to you,
		// but it must be in a bespoke format and should match the
		// load function.
		FileOutputStream bespokeOut = new FileOutputStream(file);
		ObjectOutputStream bookCollectionOut = new ObjectOutputStream(bespokeOut);
		for (Book b : this.getList()){
			bookCollectionOut.writeObject(b);
		}
		bookCollectionOut.close();
	}

	/**
	 * Saves this collection to the specified JSON file.
	 *
	 * @param file The path to a file.
	 */
	public void saveToJSONFile(File file) throws IOException {
		// TODO: Implement this function yourself. The specific hierarchy is up to you,
		// but it must be in a JSON format and should match the load function.
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try (FileWriter writer = new FileWriter(file)) {
			gson.toJson(books, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves this collection to the specified XML file.
	 *
	 * @param file The path to a file.
	 */
	public void saveToXMLFile(File file) throws ParserConfigurationException, TransformerException {
		// TODO: Implement this function yourself. The specific hierarchy is up to you,
		// but it must be in an XML format and should match the
		// load function.
		DocumentBuilderFactory dF = DocumentBuilderFactory.newInstance();
		DocumentBuilder dB = dF.newDocumentBuilder();
		Document document = dB.newDocument();

		Element r = document.createElement("collection");
		document.appendChild(r);

		for (Book b : books){
			Element book = document.createElement("book");
			r.appendChild(book);

			Element title = document.createElement("title");
			title.appendChild(document.createTextNode(b.title));
			book.appendChild(title);

			Element author = document.createElement("author");
			author.appendChild(document.createTextNode(b.authorName));
			book.appendChild(author);

			Element release = document.createElement("year");
			release.appendChild(document.createTextNode(String.valueOf(b.yearReleased)));
			book.appendChild(release);

			Element genre = document.createElement("genre");
			genre.appendChild(document.createTextNode(String.valueOf(b.bookGenre)));
			book.appendChild(genre);
		}

		Transformer t = TransformerFactory.newInstance().newTransformer();

		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(file);
		t.transform(domSource, streamResult);
	}

	/**
	 * Load a pre-existing book collection from a "bespoke" file.
	 *
	 * @param file The file to load from. This is guaranteed to exist.
	 * @return An initialised book collection.
	 */
	public static BookCollection loadFromBespokeFile(File file) throws IOException, ClassNotFoundException {
		// TODO: Implement this function yourself.
		FileInputStream bespokeIn = new FileInputStream(file);
		ObjectInputStream bookCollectionIn = new ObjectInputStream(bespokeIn);
		ArrayList<Book> temp = new ArrayList<>();
		try{
			while (bespokeIn.available() > 0){
				Book tmp = (Book) bookCollectionIn.readObject();
				temp.add(tmp);
			}
			bookCollectionIn.close();
		} catch(EOFException eof){
			eof.printStackTrace();
		}
		BookCollection books = new BookCollection();
		books.setList(temp);
		return books;
	}

	/**
	 * Load a pre-existing book collection from a JSON file.
	 *
	 * @param file The file to load from. This is guaranteed to exist.
	 * @return An initialised book collection.
	 */
	public static BookCollection loadFromJSONFile(File file) {
		// TODO: Implement this function yourself.
		Gson gson = new Gson();
		final Type LIST_TYPE = new TypeToken<List<Book>>() {
		}.getType();
		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<Book> temp = gson.fromJson(reader, LIST_TYPE);
		BookCollection books = new BookCollection();
		books.setList(temp);
		return books;
	}

	/**
	 * Load a pre-existing book collection from an XML file.
	 *
	 * @param file The file to load from. This is guaranteed to exist.
	 * @return An initialised book collection.
	 */
	public static BookCollection loadFromXMLFile(File file) throws IOException, ParserConfigurationException, SAXException {
		// TODO: Implement this function yourself.
//		DocumentBuilderFactory dF = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = dF.newDocumentBuilder();
//		Document document = builder.parse(file);
//		document.getDocumentElement().normalize();
//		NodeList temp = document.getElementsByTagName("book");
//		ArrayList<Book> bks = new ArrayList<>();
//		for (int i = 0; i < temp.getLength(); i++) {
//			Node node = temp.item(i);
//			System.out.println("Node = " + node);
//			if (node.getNodeType() == Node.ELEMENT_NODE) {
//				Element elem = (Element) node;
//				String title = elem.getElementsByTagName("title").item(0).getTextContent();
//				String author = elem.getElementsByTagName("author").item(0).getTextContent();
//				int year = Integer.parseInt(elem.getElementsByTagName("year").item(0).getTextContent());
//				String genre = elem.getElementsByTagName("genre").item(0).getTextContent();
//				Book book = new Book(title, author, year, genre);
//				System.out.println("title = " + book.title);
//				bks.add(book);
//			}
//		}
//		BookCollection books = new BookCollection();
//		books.setList(bks);
//		return books;

		List<Book> loadData = new ArrayList<>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try  {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			NodeList nl = doc.getElementsByTagName("book");

			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				//System.out.println("Node = " + n);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) n;
					String title = elem.getElementsByTagName("title").item(0).getTextContent();
					//System.out.println("title = "+title);
					String author = elem.getElementsByTagName("author").item(0).getTextContent();
					//System.out.println("author = "+author);
					int year = Integer.parseInt(elem.getElementsByTagName("year").item(0).getTextContent());
					//System.out.println("year = "+ year);
					String gen = elem.getElementsByTagName("genre").item(0).getTextContent();
					BookGenre genre = BookGenre.valueOf(gen);
					//System.out.println("genre = "+genre);
					Book book = new Book(title, author, year, genre);
					loadData.add(book);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(loadData);
		BookCollection books = new BookCollection();
		books.setList(loadData);
		return books;
	}

	public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException, SAXException {
		BookCollection collection;
		ArrayList<Book> books = new ArrayList<>();
		books.add(new Book("Thinking in Java (4th ed.)", "Eckel, Bruce", 2006, BookGenre.NON_FICTION));
		books.add(new Book("The Ultimate Hitchhiker's Guide to the Galaxy", "Adams, Douglas", 1979,
				BookGenre.FICTION_COMEDY));
		books.add(new Book("The Hobbit", "Tolkien, J.R.R.", 1937, BookGenre.FICTION_FANTASY));

		collection = new BookCollection(books);
		File file = new File("my_book_collection.xml");
 		collection.saveToXMLFile(file);
		BookCollection Newdata = loadFromXMLFile(file);

		List<Book> data = Newdata.getList();

		System.out.println("Book Data Loaded");
		for (Book c : data) {
			System.out.println("Title: " + c.title + ", Author: " + c.authorName + ", Year: " + c.yearReleased
					+ ", Genre: " + c.bookGenre);
		}
	}
}
