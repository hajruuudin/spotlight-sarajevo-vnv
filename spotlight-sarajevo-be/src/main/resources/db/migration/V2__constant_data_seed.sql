-- CREATING DEFAULT, NON MUTABLE, CATEGORIES FOR SPOTS
ALTER SEQUENCE ss_spot_category_id_seq RESTART WITH 1000;
INSERT INTO ss_spot_category (
    id,
    spot_category_name_bs,
    spot_category_name_en,
    spot_category_description_bs,
    spot_category_description_en,
    spot_category_color_code
) VALUES
-- 1001
(1001, '🍔 Brza Hrana', '🍔 Fast Food',
 'Mjesta gdje možete brzo pojesti nešto ukusno uz pristupačne cijene. Idealno za osobe u žurbi ili sa ograničenim vremenom.',
 'Places where you can quickly grab something tasty at affordable prices. Perfect for people on the go or with limited time.',
 '#66CCCC'),

-- 1002
(1002, '🍽️ Restoran', '🍽️ Restaurant',
 'Mjesta koja nude raznovrsna jela i ugodnu atmosferu za ručak ili večeru. Idealno za porodična i poslovna okupljanja.',
 'Places offering diverse meals and a pleasant atmosphere for lunch or dinner. Ideal for family and business gatherings.',
 '#6699CC'),

-- 1003
(1003, '🫖 Kafić', '🫖 Cafe',
 'Mjesta za opuštanje uz kafu, čaj ili lagani obrok. Popularna za druženje i kratke pauze tokom dana.',
 'Places to relax with coffee, tea, or a light meal. Popular for socializing and short breaks during the day.',
 '#66B2FF'),

-- 1004
(1004, '🏛️ Muzej', '🏛️ Museum',
 'Institucije koje čuvaju i prikazuju kulturno-historijsko naslijeđe Sarajeva. Idealne za edukativne i turističke posjete.',
 'Institutions preserving and displaying Sarajevo’s cultural and historical heritage. Ideal for educational and tourist visits.',
 '#66A3A3'),

-- 1005
(1005, '🏰 Historijsko Mjesto', '🏰 Historical Site',
 'Lokacije od značaja za historiju grada i zemlje. Često uključuju spomenike, mostove i stare građevine.',
 'Locations significant to the history of the city and country. Often include monuments, bridges, and old buildings.',
 '#5FA3B2'),

-- 1006
(1006, '🍷 Kafana', '🍷 Kafana',
 'Tradicionalna balkanska mjesta gdje se služi domaća hrana i piće. Često nude muziku uživo i opuštenu atmosferu.',
 'Traditional Balkan places serving local food and drinks. Often feature live music and a relaxed atmosphere.',
 '#669999'),

-- 1007
(1007, '🎶 Noćni Klub', '🎶 Night Club',
 'Mjesta za zabavu uz ples i muziku do kasno u noć. Privlače mlađu publiku i ljubitelje noćnog života.',
 'Places for dancing and music until late at night. Attract younger crowds and nightlife enthusiasts.',
 '#6666CC'),

-- 1008
(1008, '🍺 Pub', '🍺 Pub',
 'Mjesta sa bogatim izborom piva i jednostavnom hranom. Idealna za druženje u neformalnom okruženju.',
 'Places offering a wide range of beers and simple food. Perfect for casual hangouts.',
 '#6699AA'),

-- 1009
(1009, '🎤 Koncertna Dvorana', '🎤 Concert Venue',
 'Prostori namijenjeni održavanju koncerata i muzičkih događaja. Mogu biti na otvorenom ili zatvorenom prostoru.',
 'Venues designed for concerts and music events. They can be indoors or outdoors.',
 '#6666FF'),

-- 1010
(1010, '💪 Fitness Centar', '💪 Fitness Center',
 'Objekti opremljeni spravama za vježbanje i rekreaciju. Namijenjeni osobama koje žele održavati fizičku formu.',
 'Facilities equipped for exercise and recreation. Designed for people who want to stay in shape.',
 '#66CCCC'),

-- 1011
(1011, '🏊 Bazen', '🏊 Swimming Pool',
 'Mjesta za plivanje i rekreativne aktivnosti u vodi. Pogodna za sve uzraste tokom cijele godine.',
 'Places for swimming and water recreation. Suitable for all ages throughout the year.',
 '#33CCCC'),

-- 1012
(1012, '💨 Nargila Bar', '💨 Hookah Lounge',
 'Mjesta gdje se posjetioci mogu opustiti uz nargilu i laganu muziku. Često nude tople napitke i deserte.',
 'Places where visitors can relax with hookah and light music. Often serve hot drinks and desserts.',
 '#6699CC'),

-- 1013
(1013, '🥟 Buregdžinica', '🥟 Burek Shop',
 'Tradicionalna mjesta koja nude domaće pite poput bureka, sirnice i krompiruše. Popularna za brzi, topli obrok.',
 'Traditional places serving local pies such as burek, cheese pie, and potato pie. Popular for a quick warm meal.',
 '#66A3CC'),

-- 1014
(1014, '🥘 Aščinica', '🥘 Traditional Eatery',
 'Mjesta sa autentičnim bosanskim jelima posluženim na tradicionalan način. Ističu domaće recepte i ukuse.',
 'Places offering authentic Bosnian dishes served traditionally. Highlight local recipes and flavors.',
 '#6699B2'),

-- 1015
(1015, '🥩 Ćevabdžinica', '🥩 Cevapi Grill',
 'Specijalizovana mjesta koja nude poznate bosanske ćevape. Obavezna destinacija za ljubitelje mesa.',
 'Specialized spots serving famous Bosnian cevapi. A must-visit for meat lovers.',
 '#5FA3CC'),

-- 1016
(1016, '🍰 Poslastičarnica', '🍰 Dessert Locale',
 'Mjesta za ljubitelje kolača, sladoleda i drugih slatkiša. Idealna za popodnevne posjete i porodične trenutke.',
 'Places for lovers of cakes, ice cream, and other sweets. Ideal for afternoon visits and family moments.',
 '#66B2B2'),

-- 1017
(1017, '🎬 Kino', '🎬 Cinema',
 'Prostori za prikazivanje domaćih i stranih filmova. Nude udobna sjedišta i modernu opremu za projekciju.',
 'Spaces for screening domestic and international films. Offer comfortable seating and modern projection equipment.',
 '#6699FF'),

-- 1018
(1018, '🎭 Pozorište', '🎭 Theater',
 'Mjesta gdje se izvode dramske predstave i kulturni programi. Često središta umjetničkog života grada.',
 'Places hosting theatrical plays and cultural performances. Often centers of the city’s artistic life.',
 '#6666AA'),

-- 1019
(1019, '🕌 Džamija', '🕌 Mosque',
 'Vjerski objekti islamske zajednice i važni kulturni simboli Sarajeva. Otvoreni za vjernike i turiste.',
 'Religious buildings of the Islamic community and important cultural symbols of Sarajevo. Open to worshippers and tourists.',
 '#33A3A3'),

-- 1020
(1020, '⛪ Crkva', '⛪ Church',
 'Mjesta bogosluženja i duhovnog mira. Ujedno su dio bogatog multikonfesionalnog identiteta grada.',
 'Places of worship and spiritual peace. They also represent the city’s rich multiconfessional identity.',
 '#5F99B2'),

-- 1021
(1021, '🛍️ Tržni Centar', '🛍️ Shopping Mall',
 'Veliki kompleksi sa prodavnicama, restoranima i zabavom. Omiljene destinacije za kupovinu i slobodno vrijeme.',
 'Large complexes with shops, restaurants, and entertainment. Favorite destinations for shopping and leisure.',
 '#66CCCC'),

-- 1022
(1022, '🌳 Park', '🌳 Park',
 'Zelene površine za šetnju, rekreaciju i opuštanje. Omiljena mjesta za porodična okupljanja i piknike.',
 'Green areas for walking, recreation, and relaxation. Popular spots for family gatherings and picnics.',
 '#5FA399'),

-- 1023
(1023, '🖼️ Galerija', '🖼️ Gallery',
 'Mjesta koja izlažu umjetnička djela lokalnih i međunarodnih autora. Idealna za ljubitelje umjetnosti i kulture.',
 'Places exhibiting artworks by local and international artists. Ideal for art and culture enthusiasts.',
 '#9999CC'),

-- 1024
(1024, '🎮 Gaming Centar', '🎮 Gaming Center',
 'Mjesta sa konzolama i računarima za ljubitelje video igara. Pružaju zabavu i druženje uz takmičarski duh.',
 'Places equipped with consoles and PCs for gaming enthusiasts. Offer fun and social competition.',
 '#6699AA'),

-- 1025
(1025, '🏔️ Vidikovac', '🏔️ Viewpoint',
 'Mjesta sa panoramskim pogledom na grad Sarajevo i okolne planine. Omiljena destinacija za fotografisanje.',
 'Places offering panoramic views of Sarajevo and surrounding mountains. Popular destinations for photography.',
 '#66A3B2')
ON CONFLICT (id) DO NOTHING;

-- CREATING DEFAULT, NON MUTABLE, CATEGORIES FOR EVENTS
ALTER SEQUENCE ss_event_category_id_seq RESTART WITH 2000;
INSERT INTO ss_event_category (
    id,
    event_category_name_bs,
    event_category_name_en,
    event_category_description_bs,
    event_category_description_en,
    event_category_color_code
) VALUES
-- 2001
(2001, '🎵 Koncert', '🎵 Concert',
 'Događaji sa izvođenjem muzike uživo za publiku. Mogu se održavati u dvoranama ili na otvorenom prostoru.',
 'Events featuring live music performances for an audience. Can take place indoors or outdoors.',
 '#FF9966'),

-- 2002
(2002, '🎸 Live Muzika', '🎸 Live Music',
 'Manji nastupi bendova ili solo izvođača uživo. Popularno za intimnija druženja i kulturne večeri.',
 'Smaller performances by bands or solo artists live. Popular for intimate gatherings and cultural evenings.',
 '#FF6666'),

-- 2003
(2003, '🤝 Humanitarni', '🤝 Charity',
 'Događaji organizovani za prikupljanje pomoći i podrške onima kojima je potrebna. Pružaju priliku za uključivanje lokalne zajednice.',
 'Events organized to raise support and aid for those in need. Offer opportunities for local community engagement.',
 '#FF6666'),

-- 2004
(2004, '🎨 Kulturni Događaj', '🎨 Cultural Event',
 'Manifestacije koje promovišu umjetnost, tradiciju i kulturni identitet Sarajeva. Mogu uključivati izložbe, radionice ili performanse.',
 'Events promoting art, tradition, and Sarajevo’s cultural identity. May include exhibitions, workshops, or performances.',
 '#FF9966'),

-- 2005
(2005, '⚽ Sportski Događaj', '⚽ Sports Event',
 'Takmičenja ili demonstracije u sportu za publiku i učesnike. Popularno za navijače i aktivne članove zajednice.',
 'Competitions or demonstrations in sports for audience and participants. Popular for fans and active community members.',
 '#FF6633'),

-- 2006
(2006, '🗣️ Otvorena Prezentacija', '🗣️ Open Presentation',
 'Javne prezentacije, predavanja ili govorni nastupi. Namenjeno edukaciji i informisanju lokalne zajednice.',
 'Public presentations, lectures, or speaking events. Intended for education and community awareness.',
 '#FF9966'),

-- 2007
(2007, '👥 Zajedničko Okupljanje', '👥 Community Gathering',
 'Događaji koji okupljaju članove lokalne zajednice. Fokusirani su na umrežavanje, druženje i zajedničke aktivnosti.',
 'Events that bring together members of the local community. Focused on networking, socializing, and shared activities.',
 '#FF6699'),

-- 2008
(2008, '💖 Humanitarna Akcija', '💖 Humanitarian Action',
 'Inicijative za pružanje pomoći ugroženima ili zajednici. Često uključuju volontiranje i donacije.',
 'Initiatives providing aid to the vulnerable or the community. Often involve volunteering and donations.',
 '#FF6666'),

-- 2009
(2009, '🎲 Veče Igara', '🎲 Game Night',
 'Društvene igre, turniri i zabava za sve uzraste. Idealno za prijateljska okupljanja i lokalne klubove.',
 'Board games, tournaments, and fun for all ages. Perfect for friends’ gatherings and local clubs.',
 '#FF9966'),

-- 2010
(2010, '🏆 Sarajevo Tradicija', '🏆 Sarajevo Tradition',
 'Događaji koji su dugogodišnja tradicija u gradu, poput filmskih festivala i manifestacija. Važni su za kulturološki identitet Sarajeva.',
 'Events that are long-standing traditions in the city, such as film festivals and regular cultural celebrations. Important for Sarajevo’s cultural identity.',
 '#FF6633'),

-- 2011
(2011, '🇧🇦 Državni Praznik', '🇧🇦 Country Holiday',
 'Svečanosti i aktivnosti povodom državnih praznika, poput Dana nezavisnosti. Prikazuju važnost historijskih datuma i okupljaju građane.',
 'Celebrations and activities on national holidays, such as Independence Day. Highlight important historical dates and gather citizens.',
 '#FF9966'),

-- 2012
(2012, '🎭 Festival', '🎭 Festival',
 'Manifestacije sa bogatim programom uključujući muziku, umjetnost i hranu. Privlače lokalne i međunarodne posjetioce.',
 'Events with rich programming including music, art, and food. Attract local and international visitors.',
 '#FF6699'),

-- 2013
(2013, '🎤 Predavanje', '🎤 Lecture',
 'Edukativni događaji sa stručnim govornicima i prezentacijama. Pogodni za studente i profesionalce.',
 'Educational events with expert speakers and presentations. Suitable for students and professionals.',
 '#FF6666'),

-- 2014
(2014, '🎬 Filmska Projekcija', '🎬 Film Screening',
 'Prikazivanje filmova u kinima, kulturnim centrima ili na otvorenom. Fokusirano na umjetnički ili edukativni sadržaj.',
 'Screenings of films in cinemas, cultural centers, or outdoors. Focused on artistic or educational content.',
 '#FF9933'),

-- 2015
(2015, '🎪 Sajam', '🎪 Fair / Expo',
 'Događaji sa štandovima, proizvodima i interaktivnim sadržajem. Popularno za porodične i poslovne posjete.',
 'Events with booths, products, and interactive content. Popular for family and business visits.',
 '#FF6666'),

-- 2016
(2016, '🛍️ Lokalna Tržnica', '🛍️ Local Market',
 'Događaji koji okupljaju lokalne proizvođače i zanatlije. Idealno za kupovinu domaćih proizvoda i rukotvorina.',
 'Events gathering local producers and artisans. Perfect for purchasing local products and crafts.',
 '#FF9966'),

-- 2017
(2017, '🎶 DJ Veče', '🎶 DJ Night',
 'Događaji sa DJ nastupima i plesom. Popularno za mlade i ljubitelje elektronske muzike.',
 'Events featuring DJ performances and dancing. Popular with young people and electronic music lovers.',
 '#FF6699')
ON CONFLICT (id) DO NOTHING;

-- CREATING DEFAULT, NON MUTABLE, TAGS FOR BOTH EVENTS AND SPOTS TOGETHER
-- These describe the essence of the spot or the event and some traits that are important to the object
ALTER SEQUENCE ss_tag_id_seq RESTART WITH 3000;
INSERT INTO ss_tag (
    id,
    tag_name_bs,
    tag_name_en,
    tag_description_bs,
    tag_description_en
) VALUES
-- 3001
(3001, '👨‍👩‍👧 Porodično Prijateljski', '👨‍👩‍👧 Family Friendly',
 'Prikladno za sve članove porodice i djecu.',
 'Suitable for all family members and children.'),

-- 3002
(3002, '🍷 Alkohol Dostupan', '🍷 Pro-Alcohol',
 'Mogućnost konzumacije alkoholnih pića na licu mjesta.',
 'Availability of alcoholic beverages on-site.'),

-- 3003
(3003, '🥘 Tradicionalna Jela', '🥘 Traditional Meals',
 'Poslužuju lokalna jela sa autentičnim okusima.',
 'Serves local dishes with authentic flavors.'),

-- 3004
(3004, '💸 Pristupačno', '💸 Cheap',
 'Cijene su pristupačne širokom krugu posjetilaca.',
 'Prices are affordable for a wide audience.'),

-- 3005
(3005, '💎 Luksuzno', '💎 Luxurious',
 'Ponuda i ambijent sa visokim standardom kvaliteta.',
 'High-standard quality in offerings and ambiance.'),

-- 3006
(3006, '🎶 Muzika Uživo', '🎶 Live Music',
 'Događaji ili lokacije sa nastupima uživo.',
 'Events or places featuring live performances.'),

-- 3007
(3007, '☕ Kafa / Piće', '☕ Coffee / Drink',
 'Mogućnost uživanja u napicima i kafama.',
 'Opportunity to enjoy drinks and coffee.'),

-- 3008
(3008, '🌿 Prirodno / Outdoor', '🌿 Outdoor / Nature',
 'Otvoreni prostori i mjesta u prirodi.',
 'Open spaces and locations in nature.'),

-- 3009
(3009, '🎉 Zabava', '🎉 Entertainment',
 'Idealno za zabavu i druženje sa prijateljima.',
 'Perfect for fun and socializing with friends.'),

-- 3010
(3010, '🧘‍♂️ Relaksacija', '🧘‍♂️ Relaxation',
 'Mjesta pogodna za opuštanje i odmor.',
 'Places suitable for relaxation and rest.'),

-- 3011
(3011, '🏃‍♂️ Aktivno', '🏃‍♂️ Active',
 'Za sve koji žele fizičku aktivnost i sport.',
 'For those seeking physical activity and sports.'),

-- 3012
(3012, '🎭 Kultura', '🎭 Cultural',
 'Manifestacije sa umjetničkim i kulturnim sadržajem.',
 'Events with artistic and cultural content.'),

-- 3013
(3013, '🛍️ Kupovina', '🛍️ Shopping',
 'Mjesta pogodna za kupovinu proizvoda i suvenira.',
 'Places suitable for purchasing goods and souvenirs.'),

-- 3014
(3014, '🥂 Elegantno', '🥂 Elegant',
 'Lokacije sa sofisticiranim ambijentom i stilom.',
 'Locations with a sophisticated ambiance and style.'),

-- 3015
(3015, '🌙 Noćni Život', '🌙 Nightlife',
 'Aktivnosti i lokacije popularne noću.',
 'Activities and places popular during the night.'),

-- 3016
(3016, '🖼️ Umjetnost', '🖼️ Art',
 'Prikaz ili izložba umjetničkih djela.',
 'Exhibition or display of artistic works.'),

-- 3017
(3017, '📚 Edukacija', '📚 Educational',
 'Događaji ili lokacije sa edukativnim sadržajem.',
 'Events or locations with educational content.'),

-- 3018
(3018, '🥳 Društveno', '🥳 Social',
 'Mjesta pogodna za okupljanje i druženje.',
 'Places suitable for gathering and socializing.'),

-- 3019
(3019, '🌐 Internacionalno', '🌐 International',
 'Privlači posjetioce iz drugih zemalja.',
 'Attracts visitors from other countries.'),

-- 3020
(3020, '🆓 Besplatno', '🆓 Free Entry',
 'Ulaz ili pristup bez naknade.',
 'Entry or access without any charge.'),

-- 3021
(3021, '🌶️ Egzotično', '🌶️ Exotic',
 'Ponuda ili iskustvo koje je neobično i zanimljivo.',
 'Offering or experience that is unusual and interesting.'),

-- 3022
(3022, '📅 Redovno / Tradicionalno', '📅 Regular / Traditional',
 'Događaji koji se održavaju periodično ili su dio tradicije.',
 'Events that occur periodically or are part of tradition.'),

-- 3023
(3023, '🐾 Prijateljski za Ljubimce', '🐾 Pet Friendly',
 'Moguće je posjetiti sa kućnim ljubimcima.',
 'Possible to visit with pets.'),

-- 3024
(3024, '🛋️ Udobno', '🛋️ Cozy',
 'Mjesta sa udobnim sjedenjem i opuštenim ambijentom.',
 'Places with comfortable seating and relaxed atmosphere.'),

-- 3025
(3025, '🎬 Film / Kino', '🎬 Film / Cinema',
 'Projekcije filmova i filmski programi.',
 'Film screenings and movie programs.'),

-- 3026
(3026, '🎤 Predavanja / Talk', '🎤 Lectures / Talk',
 'Edukativni ili motivacioni govori i prezentacije.',
 'Educational or motivational talks and presentations.'),

-- 3027
(3027, '🍹 Kokteli', '🍹 Cocktails',
 'Mogućnost uživanja u koktelima i pićima.',
 'Opportunity to enjoy cocktails and drinks.'),

-- 3028
(3028, '🌳 Priroda', '🌳 Nature',
 'Lokacije u prirodnom okruženju ili parkovima.',
 'Locations in natural environments or parks.'),

-- 3029
(3029, '🎸 Muzika / Band', '🎸 Music / Band',
 'Događaji sa nastupima bendova ili solo izvođača.',
 'Events with band or solo music performances.'),

-- 3030
(3030, '🏞️ Pogled', '🏞️ Scenic View',
 'Mjesta sa lijepim pogledom ili panoramom.',
 'Places with beautiful views or panoramas.'),

-- 3031
(3031, '🕺 Ples', '🕺 Dance',
 'Događaji ili lokacije pogodna za ples i zabavu.',
 'Events or places suitable for dancing and fun.'),

-- 3032
(3032, '🍔 Brza Hrana', '🍔 Fast Food',
 'Ponuda brze hrane za brzu i praktičnu konzumaciju.',
 'Fast food offerings for quick and convenient eating.'),

-- 3033
(3033, '🥗 Zdrava Hrana', '🥗 Healthy Food',
 'Opcije zdrave i nutritivne hrane.',
 'Options for healthy and nutritious meals.'),

-- 3034
(3034, '🖥️ Digitalno / Gaming', '🖥️ Digital / Gaming',
 'Mjesta sa video igrama ili digitalnim sadržajem.',
  'Places with video games or digital content.'),

-- 3035
(3035, '🏋️‍♂️ Fitness', '🏋️‍♂️ Fitness',
 'Mjesta pogodna za vježbanje i sport.',
 'Places suitable for exercise and sports.'),

-- 3036
(3036, '💡 Edukacija', '💡 Educational',
 'Događaji ili lokacije sa edukativnim sadržajem.',
 'Events or locations with educational content.'),

-- 3037
(3037, '👗 Moda / Stil', '👗 Fashion / Style',
 'Lokacije ili događaji vezani za modu i stil.',
 'Places or events related to fashion and style.'),

-- 3038
(3038, '🎁 Suveniri', '🎁 Souvenirs',
 'Mogućnost kupovine lokalnih suvenira i proizvoda.',
 'Opportunity to purchase local souvenirs and products.'),

-- 3039
(3039, '🛶 Aktivnosti na Vodi', '🛶 Water Activities',
 'Događaji ili lokacije sa aktivnostima na rijekama ili jezerima.',
 'Events or locations with activities on rivers or lakes.'),

-- 3040
(3040, '🎯 Takmičenje', '🎯 Competition',
 'Događaji sa takmičenjima ili izazovima za učesnike.',
 'Events with competitions or challenges for participants.'),

-- 3041
(3041, '🌟 Popularno', '🌟 Popular',
 'Mjesta ili događaji koji su često posjećeni i poznati.',
 'Places or events that are frequently visited and well-known.'),

-- 3042
(3042, '🎶 Instrumentalno', '🎶 Instrumental',
 'Događaji sa nastupima instrumentalne muzike.',
 'Events with instrumental music performances.'),

-- 3043
(3043, '📸 Fotogenično', '📸 Photogenic',
 'Lokacije ili događaji idealni za fotografisanje.',
 'Places or events perfect for taking photos.'),

-- 3044
(3044, '🕰️ Historijski', '🕰️ Historical',
 'Mjesta ili događaji sa historijskim značajem.',
 'Places or events with historical significance.'),

-- 3045
(3045, '🎮 E-sport', '🎮 E-sport',
 'Događaji vezani za elektronske sportske turnire.',
 'Events related to electronic sports tournaments.'),

-- 3046
(3046, '🎨 Radionica', '🎨 Workshop',
 'Događaji gdje učesnici mogu učiti i stvarati.',
 'Events where participants can learn and create.'),

-- 3047
(3047, '🧑‍🍳 Gastronomija', '🧑‍🍳 Gastronomy',
 'Događaji sa fokusom na hranu i kulinarska iskustva.',
 'Events focused on food and culinary experiences.'),

-- 3048
(3048, '🛴 Aktivnosti na Otvorenom', '🛴 Outdoor Activities',
 'Događaji i lokacije sa aktivnostima van zatvorenog prostora.',
 'Events and places with outdoor activities.'),

-- 3049
(3049, '🎺 Jazz / Blues', '🎺 Jazz / Blues',
 'Događaji sa jazz ili blues muzikom.',
 'Events featuring jazz or blues music.'),

-- 3050
(3050, '🌞 Dnevni Program', '🌞 Daytime Program',
 'Aktivnosti ili događaji koji se održavaju tokom dana.',
 'Activities or events that take place during the day.'),

-- 3051
(3051, '🌙 Večernji Program', '🌙 Evening Program',
  'Aktivnosti ili događaji koji se održavaju navečer.',
  'Activities or events that take place in the evening.')
ON CONFLICT (id) DO NOTHING;
