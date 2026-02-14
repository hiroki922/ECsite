-- ダミーデータ投入（起動時に自動実行）
-- パスワードはすべて "password" (BCrypt encoded)

-- ユーザーデータ
INSERT IGNORE INTO user (id, name, email, password, role) VALUES
(1, '管理者太郎', 'admin@example.com', '$2a$10$EIc4gGfeBvQIFBm3G1Hq9OGeRXOSqPQFoJqkEDLLkRS9sKkJEYb0S', 'ADMIN'),
(2, '山田花子', 'hanako@example.com', '$2a$10$EIc4gGfeBvQIFBm3G1Hq9OGeRXOSqPQFoJqkEDLLkRS9sKkJEYb0S', 'USER'),
(3, '佐藤一郎', 'ichiro@example.com', '$2a$10$EIc4gGfeBvQIFBm3G1Hq9OGeRXOSqPQFoJqkEDLLkRS9sKkJEYb0S', 'USER'),
(4, '鈴木美咲', 'misaki@example.com', '$2a$10$EIc4gGfeBvQIFBm3G1Hq9OGeRXOSqPQFoJqkEDLLkRS9sKkJEYb0S', 'USER'),
(5, '田中健太', 'kenta@example.com', '$2a$10$EIc4gGfeBvQIFBm3G1Hq9OGeRXOSqPQFoJqkEDLLkRS9sKkJEYb0S', 'USER');

-- 商品データ
INSERT IGNORE INTO products (id, name, description, price, image_url, stock) VALUES
(1, 'ワイヤレスイヤホン Pro', '高音質ノイズキャンセリング搭載。最大24時間再生対応。', 12800, 'https://placehold.co/400x400?text=Earphones', 50),
(2, 'スマートウォッチ X1', '心拍数・睡眠トラッキング機能付き。防水IPX7対応。', 24800, 'https://placehold.co/400x400?text=SmartWatch', 30),
(3, 'USBーC 急速充電器 65W', 'ノートPC・スマホ対応。GaN採用でコンパクト設計。', 3980, 'https://placehold.co/400x400?text=Charger', 100),
(4, 'メカニカルキーボード RGB', '赤軸採用。フルRGBバックライト。テンキーレス。', 8900, 'https://placehold.co/400x400?text=Keyboard', 25),
(5, 'ゲーミングマウス Light', '超軽量63g。最大16000DPI。6ボタン搭載。', 5480, 'https://placehold.co/400x400?text=Mouse', 40),
(6, '4K モニター 27インチ', 'IPS液晶パネル。HDR400対応。USB-Cハブ機能付き。', 39800, 'https://placehold.co/400x400?text=Monitor', 15),
(7, 'ノイズキャンセリングヘッドホン', 'オーバーイヤー型。最大30時間再生。折りたたみ収納可能。', 19800, 'https://placehold.co/400x400?text=Headphones', 20),
(8, 'ポータブルSSD 1TB', 'USB 3.2 Gen2対応。読み込み最大1050MB/s。耐衝撃設計。', 11800, 'https://placehold.co/400x400?text=SSD', 35),
(9, 'Webカメラ 4K対応', 'オートフォーカス搭載。内蔵マイク付き。リモート会議に最適。', 7800, 'https://placehold.co/400x400?text=Webcam', 45),
(10, 'スマホスタンド アルミ製', '角度調整可能。折りたたみ式。タブレットにも対応。', 1980, 'https://placehold.co/400x400?text=Stand', 80);

-- 住所データ
INSERT IGNORE INTO addresses (id, name, postal_code, prefecture, city, address_line, phone, is_default, user_id) VALUES
(1, '山田花子', '150-0001', '東京都', '渋谷区', '神宮前1-2-3 サンプルマンション101', '090-1234-5678', true, 2),
(2, '山田花子', '160-0022', '東京都', '新宿区', '新宿3-4-5', '090-1234-5678', false, 2),
(3, '佐藤一郎', '530-0001', '大阪府', '大阪市北区', '梅田2-5-10 グランドビル502', '080-9876-5432', true, 3),
(4, '鈴木美咲', '460-0008', '愛知県', '名古屋市中区', '栄3-1-1 サカエタワー1203', '070-1111-2222', true, 4),
(5, '田中健太', '812-0011', '福岡県', '福岡市博多区', '博多駅前2-8-1 博多プラザ305', '090-3333-4444', true, 5);
