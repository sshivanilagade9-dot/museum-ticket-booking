INSERT INTO museum (museum_id, name, location, open_days, open_time, close_time, closed_on, description, adult_price, child_price, student_price) VALUES
(1,'Raja Dinkar Kelkar Museum','Shukrawar Peth, Pune','Monday to Sunday','10:00 AM','5:30 PM','Open all days','Over 20,000 everyday Indian craft objects, musical instruments and ornate doors.',100,30,100),
(2,'Tribal Cultural Museum','Koregaon Road, Camp, Pune','Tuesday to Sunday (sample)','10:00 AM','5:00 PM','Monday (sample)','Tribal artefacts, masks, jewellery and replica huts.',50,25,50),
(3,'Vikram Pendse Cycle Museum','Karve Nagar, Pune','Daily (sample)','10:00 AM','5:00 PM','None (sample)','A journey through the history of bicycles.',100,100,100),
(4,'Mahatma Phule Museum','Central Pune','Tuesday to Sunday (sample)','8:00 AM','5:00 PM','Monday (sample)','Local history and the legacy of Mahatma Phule and Savitribai Phule.',20,10,10)
ON CONFLICT (museum_id) DO NOTHING;

INSERT INTO transport (transport_id, transport_type, price, available) VALUES
(1,'Shuttle bus',80,true),(2,'Cab',300,true),(3,'Metro feeder',40,false)
ON CONFLICT (transport_id) DO NOTHING;
