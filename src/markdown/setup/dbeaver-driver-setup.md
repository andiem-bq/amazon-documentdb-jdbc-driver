### DBeaver
[Link to product webpage](https://dbeaver.io).

#### Adding the Amazon DocumentDB JDBC Driver
1. If connecting from outside the DocumentDB cluster's VPC, ensure you have [setup an SSH tunnel](setup.md#using-an-ssh-tunnel-to-connect-to-amazon-documentdb).
2. Launch the **DBeaver** application.
3. In the main menu, navigate to and select **Database > Driver Manager > New**.
    1. In **Settings** tab, for **Driver Name** field, enter a descriptive name (e.g. AmazonDocDB)
    2. In **Settings** tab, for **URL Template** field, enter your [JDBC connection string](connection-string.md). 
    For example: 
    
        ~~~
        jdbc:documentdb://localhost:27017/<database-name>?tls=true&tlsAllowInvalidHostnames=true
        ~~~
       
        If your database requires an username and password, it can be entered in the connection string when setting up
        the driver
        
        ~~~
        jdbc:documentdb://<username>:<password>@localhost:27017/<database-name>?tls=true&tlsAllowInvalidHostnames=true
        ~~~
       
        or username and password can be entered later when [connecting to Amazon DocumentDB using DBeaver](#connecting-to-amazon-documentdb-using-dbeaver).
        
    3. In **Settings** tab, for **Default Port** field, enter **27017**.
    4. In **Libraries** tab, click **Add file** and navigate and select your [Amazon DocumentDB JDBC driver JAR file](setup.md#documentdb-jdbc-driver).
    5. In **Libraries** tab, after adding the JAR file, click **Find Class**.
    6. In **Settings** tab, the field **Class Name** should be automatically filled in. If not, enter the 
    **Class Name**: *software.amazon.documentdb.jdbc.DocumentDbDriver*. Click **Ok**.
    
    ![Driver Manager](../images/dbeaver/dbeaver-driver-manager.png)

#### Connecting to Amazon DocumentDB Using Dbeaver
1. In the main menu, navigate to and select **Database > New Database Connection**.
    1. Select your database/driver created above identified by **Driver Name** you chose.
2. In the **Connect to a database** prompt:
    1. In the **Main** tab, in the **Authentication** section, enter the username and password if your database requires
    it AND if it was not entered in **URL Template** when [setting up your driver](#adding-the-amazon-documentdb-jdbc-driver). 
    Otherwise you can leave it empty. The JDBC URL for your driver is shown in this prompt. 
    
    2. Click **Test Connection** to confirm your connection and then click **Finish**.

    ![Connect to DB](../images/dbeaver/dbeaver-connect-to-db.png)