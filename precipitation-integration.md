# Hướng dẫn tích hợp API Lượng mưa (Precipitation)

> **Nguồn dữ liệu:** JMA AMeDAS (Japan Meteorological Agency - Automated Meteorological Data Acquisition System)

---

## 1. Tổng quan API

| Thông tin | Chi tiết |
|-----------|----------|
| **Base URL** | `https://www.jma.go.jp/bosai/amedas` |
| **Loại API** | REST JSON (Unofficial) |
| **Real-time** | ✅ Có - Cập nhật mỗi **10 phút** |
| **Chi phí** | Miễn phí |
| **Số trạm** | ~1,300 trạm trên toàn Nhật Bản |

---

## 2. API Endpoints

### 2.1 Lấy danh sách trạm quan trắc

**Request:**
```
GET https://www.jma.go.jp/bosai/amedas/const/amedastable.json
```

**Params:** Không có

**Response mẫu:**
```json
{
  "11001": {
    "type": "C",
    "elems": "11112010",
    "lat": [45, 31.2],
    "lon": [141, 56.1],
    "alt": 26,
    "kjName": "宗谷岬",
    "knName": "ソウヤミサキ",
    "enName": "Cape Soya"
  },
  "11016": {
    "type": "A",
    "elems": "11111111",
    "lat": [45, 24.9],
    "lon": [141, 40.7],
    "alt": 3,
    "kjName": "稚内",
    "knName": "ワッカナイ",
    "enName": "Wakkanai"
  }
}
```

**Chi tiết các field:**

| Field | Type | Mô tả |
|-------|------|-------|
| `{stationId}` (key) | string | Mã trạm quan trắc (5-6 chữ số) |
| `type` | string | Loại trạm: `A` = Full station, `B`, `C` = Limited sensors |
| `elems` | string | Chuỗi 8 ký tự cho biết sensors có sẵn (xem bảng dưới) |
| `lat` | array | Vĩ độ dạng `[độ, phút]` - cần convert sang decimal |
| `lon` | array | Kinh độ dạng `[độ, phút]` - cần convert sang decimal |
| `alt` | number | Độ cao (m) |
| `kjName` | string | Tên tiếng Nhật (Kanji) |
| `knName` | string | Tên tiếng Nhật (Katakana) |
| `enName` | string | Tên tiếng Anh |

**Giải mã `elems` (8 ký tự):**

| Vị trí | Ý nghĩa | `1` = Có, `0` = Không |
|--------|---------|----------------------|
| 0 | Precipitation (lượng mưa) | ✅ |
| 1 | Temperature (nhiệt độ) | |
| 2 | Wind (gió) | |
| 3 | Sunshine (nắng) | |
| 4 | Snow (tuyết) | |
| 5 | Humidity (độ ẩm) | |
| 6 | Pressure (áp suất) | |
| 7 | Soil (đất) | |

---

### 2.2 Lấy thời gian quan trắc mới nhất

**Request:**
```
GET https://www.jma.go.jp/bosai/amedas/data/latest_time.txt
```

**Params:** Không có

**Response mẫu:**
```
2026-01-06T21:20:00+09:00
```

**Chi tiết:**
- Format: ISO 8601 với timezone JST (+09:00)
- Dùng để xây dựng URL lấy dữ liệu observation

---

### 2.3 Lấy dữ liệu quan trắc

**Request:**
```
GET https://www.jma.go.jp/bosai/amedas/data/map/{timestamp}00.json
```

**Params:**

| Param | Type | Mô tả | Ví dụ |
|-------|------|-------|-------|
| `timestamp` | string | Thời gian format `YYYYMMDDHHmm` | `202601062120` |

**Cách tạo timestamp:**
```javascript
const formatTimestamp = (isoString) => {
  const date = new Date(isoString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hour = String(date.getHours()).padStart(2, '0');
  const minute = String(Math.floor(date.getMinutes() / 10) * 10).padStart(2, '0');
  return `${year}${month}${day}${hour}${minute}`;
};

// Ví dụ: "2026-01-06T21:20:00+09:00" → "202601062120"
```

**Response mẫu:**
```json
{
  "11001": {
    "temp": [-9.0, 0],
    "humidity": [84, 0],
    "sun10m": [0, 0],
    "sun1h": [0.0, 0],
    "precipitation10m": [0.0, 0],
    "precipitation1h": [0.0, 0],
    "precipitation3h": [0.0, 0],
    "precipitation24h": [0.5, 0],
    "windDirection": [9, 0],
    "wind": [3.4, 0]
  },
  "11016": {
    "pressure": [1012.4, 0],
    "normalPressure": [1013.9, 0],
    "temp": [-8.0, 0],
    "humidity": [91, 0],
    "visibility": [16520.0, 0],
    "sun10m": [0, 0],
    "sun1h": [0.0, 0],
    "precipitation10m": [0.0, 0],
    "precipitation1h": [0.0, 0],
    "precipitation3h": [0.0, 0],
    "precipitation24h": [3.0, 0],
    "windDirection": [7, 0],
    "wind": [2.4, 0]
  }
}
```

**Chi tiết các field liên quan đến lượng mưa:**

| Field | Type | Đơn vị | Mô tả |
|-------|------|--------|-------|
| `precipitation10m` | `[value, quality]` | mm | Lượng mưa 10 phút gần nhất |
| `precipitation1h` | `[value, quality]` | mm | Lượng mưa 1 giờ gần nhất |
| `precipitation3h` | `[value, quality]` | mm | Lượng mưa 3 giờ gần nhất |
| `precipitation24h` | `[value, quality]` | mm | Lượng mưa 24 giờ gần nhất |

**Giải mã `quality` flag:**

| Giá trị | Ý nghĩa |
|---------|---------|
| `0` | Dữ liệu bình thường |
| `1` | Dữ liệu nghi ngờ |
| `2` | Dữ liệu không hợp lệ |

---

## 3. Hướng dẫn Parse dữ liệu

### 3.1 Convert tọa độ từ [độ, phút] sang decimal

```javascript
const convertToDecimalDegrees = (value) => {
  if (Array.isArray(value) && value.length === 2) {
    const [degrees, minutes] = value;
    return degrees + minutes / 60;
  }
  return value;
};

// Ví dụ: [45, 31.2] → 45 + 31.2/60 = 45.52
```

### 3.2 Extract giá trị từ response

```javascript
const getValue = (field) => {
  if (Array.isArray(field)) return field[0]; // Lấy value, bỏ quality flag
  if (typeof field === 'number') return field;
  return null;
};

// Ví dụ: [0.5, 0] → 0.5
```

### 3.3 Lọc trạm có sensor lượng mưa

```javascript
const hasPrecipitationSensor = (elems) => {
  return !elems || elems.charAt(0) === '1';
};
```

### 3.4 Phân loại mức độ mưa (JMA Standard)

```javascript
const PRECIPITATION_LEVELS = [
  { level: 0, min: 0, max: 0, color: '#CCCCCC', label: 'なし (None)' },
  { level: 1, min: 0.1, max: 1, color: '#A0D2FF', label: '弱い雨 (Light)' },
  { level: 2, min: 1, max: 5, color: '#33B5FF', label: '小雨 (Light Rain)' },
  { level: 3, min: 5, max: 10, color: '#00FF00', label: '雨 (Rain)' },
  { level: 4, min: 10, max: 20, color: '#FFFF00', label: 'やや強い雨 (Moderate)' },
  { level: 5, min: 20, max: 30, color: '#FFA500', label: '強い雨 (Heavy)' },
  { level: 6, min: 30, max: 50, color: '#FF0000', label: '激しい雨 (Very Heavy)' },
  { level: 7, min: 50, max: 80, color: '#B40068', label: '非常に激しい雨 (Intense)' },
  { level: 8, min: 80, max: Infinity, color: '#9400D3', label: '猛烈な雨 (Torrential)' },
];

const getPrecipitationLevel = (mmPerHour) => {
  if (mmPerHour === null || mmPerHour === 0) return PRECIPITATION_LEVELS[0];

  for (const level of PRECIPITATION_LEVELS) {
    if (mmPerHour >= level.min && mmPerHour < level.max) {
      return level;
    }
  }
  return PRECIPITATION_LEVELS[PRECIPITATION_LEVELS.length - 1];
};
```

---

## 4. Code mẫu hoàn chỉnh

```javascript
// Fetch và hiển thị dữ liệu lượng mưa
const fetchPrecipitationData = async () => {
  // 1. Lấy danh sách trạm
  const stationsRes = await fetch(
    'https://www.jma.go.jp/bosai/amedas/const/amedastable.json'
  );
  const stationsData = await stationsRes.json();

  // 2. Lấy thời gian mới nhất
  const timeRes = await fetch(
    'https://www.jma.go.jp/bosai/amedas/data/latest_time.txt'
  );
  const latestTime = (await timeRes.text()).trim();
  const timestamp = formatTimestamp(latestTime);

  // 3. Lấy dữ liệu quan trắc
  const dataRes = await fetch(
    `https://www.jma.go.jp/bosai/amedas/data/map/${timestamp}00.json`
  );
  const obsData = await dataRes.json();

  // 4. Parse và kết hợp dữ liệu
  const result = [];

  for (const [stationId, stationInfo] of Object.entries(stationsData)) {
    // Kiểm tra có sensor lượng mưa không
    if (!hasPrecipitationSensor(stationInfo.elems)) continue;

    // Convert tọa độ
    const lat = convertToDecimalDegrees(stationInfo.lat);
    const lon = convertToDecimalDegrees(stationInfo.lon);

    // Lấy dữ liệu observation
    const obs = obsData[stationId];
    const precipitation1h = obs ? getValue(obs.precipitation1h) : 0;
    const level = getPrecipitationLevel(precipitation1h);

    result.push({
      id: stationId,
      name: stationInfo.kjName,
      nameEn: stationInfo.enName,
      lat,
      lon,
      alt: stationInfo.alt,
      precipitation: {
        mm10m: obs ? getValue(obs.precipitation10m) : null,
        mm1h: precipitation1h,
        mm3h: obs ? getValue(obs.precipitation3h) : null,
        mm24h: obs ? getValue(obs.precipitation24h) : null,
      },
      level,
      timestamp: latestTime,
    });
  }

  return result;
};
```

---

## 5. Lấy dữ liệu lịch sử 24 giờ cho Bar Chart

### 5.1 API lấy dữ liệu theo giờ

Để tạo chart 24h như trong hình, cần gọi API nhiều lần với các timestamp khác nhau hoặc sử dụng dữ liệu tích lũy.

**Cách 1: Sử dụng dữ liệu tích lũy có sẵn**

API trả về sẵn các giá trị tích lũy:
- `precipitation1h`: Lượng mưa 1 giờ qua
- `precipitation3h`: Lượng mưa 3 giờ qua
- `precipitation24h`: Lượng mưa 24 giờ qua

**Cách 2: Gọi API cho từng giờ trong 24h**

```javascript
/**
 * Lấy dữ liệu 24 giờ qua cho một trạm
 * @param {string} stationId - Mã trạm
 * @returns {Array} Mảng 24 phần tử, mỗi phần tử là lượng mưa của 1 giờ
 */
const fetch24HourHistory = async (stationId) => {
  const history = [];
  const now = new Date();

  // Làm tròn xuống 10 phút gần nhất
  now.setMinutes(Math.floor(now.getMinutes() / 10) * 10, 0, 0);

  // Lấy dữ liệu cho 24 giờ (mỗi giờ 1 điểm)
  for (let h = 23; h >= 0; h--) {
    const targetTime = new Date(now.getTime() - h * 60 * 60 * 1000);
    const timestamp = formatTimestamp(targetTime.toISOString());

    try {
      const response = await fetch(
        `https://www.jma.go.jp/bosai/amedas/data/map/${timestamp}00.json`
      );
      const data = await response.json();

      const stationData = data[stationId];
      const value = stationData?.precipitation1h
        ? stationData.precipitation1h[0]
        : 0;

      history.push({
        hour: targetTime.getHours(),
        timestamp: targetTime.toISOString(),
        value: value || 0,
      });
    } catch (error) {
      history.push({
        hour: targetTime.getHours(),
        timestamp: targetTime.toISOString(),
        value: 0,
      });
    }
  }

  return history;
};
```

**Lưu ý quan trọng:**
- API chỉ giữ dữ liệu **10 ngày gần nhất**
- Gọi 24 requests liên tiếp có thể chậm → nên dùng Promise.all để gọi song song
- Nên cache kết quả 10 phút để tránh gọi API quá nhiều

**Tối ưu với Promise.all và Cache (Khuyến nghị):**

```javascript
// Cache cho dữ liệu 24h history
let cached24HourHistory = {}; // { stationId: { data, timestamp } }
const HISTORY_CACHE_DURATION_MS = 10 * 60 * 1000; // 10 phút

const fetch24HourHistoryOptimized = async (stationId) => {
  // Kiểm tra cache trước
  const cached = cached24HourHistory[stationId];
  if (cached && (Date.now() - cached.timestamp) < HISTORY_CACHE_DURATION_MS) {
    console.log(`Using cached 24h history for station ${stationId}`);
    return cached.data;
  }

  const now = new Date();
  now.setMinutes(Math.floor(now.getMinutes() / 10) * 10, 0, 0);

  // Tạo tất cả promises song song
  const fetchPromises = [];
  for (let h = 23; h >= 0; h--) {
    const targetTime = new Date(now.getTime() - h * 60 * 60 * 1000);
    const timestamp = formatTimestamp(targetTime.toISOString());

    fetchPromises.push(
      fetch(`https://www.jma.go.jp/bosai/amedas/data/map/${timestamp}00.json`)
        .then(res => res.json())
        .then(data => ({
          hour: targetTime.getHours(),
          timestamp: targetTime.toISOString(),
          value: data[stationId]?.precipitation1h?.[0] || 0,
        }))
        .catch(() => ({
          hour: targetTime.getHours(),
          timestamp: targetTime.toISOString(),
          value: 0,
        }))
    );
  }

  const results = await Promise.all(fetchPromises);
  const sortedResults = results.sort((a, b) =>
    new Date(a.timestamp) - new Date(b.timestamp)
  );

  // Lưu vào cache
  const result = { stationId, history: sortedResults };
  cached24HourHistory[stationId] = {
    data: result,
    timestamp: Date.now(),
  };

  return result;
};
```

### 5.2 Cấu trúc dữ liệu cho Chart Panel

```javascript
// Dữ liệu cần cho UI như trong hình
const chartData = {
  // Thông tin trạm
  station: {
    id: '35162',
    name: '新庄',
    nameEn: 'Shinjo',
    lat: 38.7567,
    lon: 140.3117,
  },

  // Dữ liệu hiện tại
  current: {
    precipitation1h: 0.5,      // mm/h - "現在の降水量"
    precipitation3h: 0.5,      // mm - "3時間"
    precipitation24h: 10.0,    // mm - "24時間"
    timestamp: '2026-01-07T00:20:00+09:00',
  },

  // Mức độ hiện tại
  level: {
    label: '弱い雨',
    labelEn: 'Light Rain',
    color: '#A0D2FF',
  },

  // Dữ liệu 24 giờ cho bar chart - "過去24時間の降水量変化"
  history: [
    { hour: 1, value: 0.5 },
    { hour: 2, value: 0 },
    { hour: 3, value: 0 },
    { hour: 4, value: 0.5 },
    { hour: 5, value: 0 },
    { hour: 6, value: 0 },
    { hour: 7, value: 1.0 },
    { hour: 8, value: 1.0 },
    { hour: 9, value: 0.5 },
    { hour: 10, value: 1.0 },
    { hour: 11, value: 0.5 },
    { hour: 12, value: 0 },
    { hour: 13, value: 0.5 },
    { hour: 14, value: 0 },
    { hour: 15, value: 0 },
    { hour: 16, value: 0.5 },
    { hour: 17, value: 0.5 },
    { hour: 18, value: 0 },
    { hour: 19, value: 0.5 },
    { hour: 20, value: 0 },
    { hour: 21, value: 0 },
    { hour: 22, value: 0.5 },
    { hour: 23, value: 1.0 },
    { hour: 0, value: 0.5 },   // Giờ hiện tại
  ],
};
```

### 5.3 Vẽ Bar Chart với Canvas

```javascript
/**
 * Vẽ bar chart lượng mưa 24h
 * @param {HTMLCanvasElement} canvas
 * @param {Array} history - Mảng 24 giá trị lượng mưa
 */
const drawPrecipitationChart = (canvas, history) => {
  const ctx = canvas.getContext('2d');
  const width = canvas.width;
  const height = canvas.height;

  // Config
  const padding = { top: 20, right: 10, bottom: 30, left: 40 };
  const chartWidth = width - padding.left - padding.right;
  const chartHeight = height - padding.top - padding.bottom;
  const barWidth = chartWidth / 24 - 2;

  // Tìm giá trị max để scale
  const maxValue = Math.max(...history.map(h => h.value), 1);
  const gridMax = Math.ceil(maxValue / 2) * 2; // Làm tròn lên số chẵn

  // Clear canvas
  ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
  ctx.fillRect(0, 0, width, height);

  // Vẽ grid lines và labels trục Y
  ctx.strokeStyle = 'rgba(255, 255, 255, 0.1)';
  ctx.fillStyle = '#888';
  ctx.font = '10px sans-serif';
  ctx.textAlign = 'right';

  const gridLines = 5;
  for (let i = 0; i <= gridLines; i++) {
    const y = padding.top + (chartHeight / gridLines) * i;
    const value = gridMax - (gridMax / gridLines) * i;

    // Grid line
    ctx.beginPath();
    ctx.moveTo(padding.left, y);
    ctx.lineTo(width - padding.right, y);
    ctx.stroke();

    // Y-axis label
    ctx.fillText(value.toString(), padding.left - 5, y + 3);
  }

  // Label trục Y
  ctx.save();
  ctx.translate(12, height / 2);
  ctx.rotate(-Math.PI / 2);
  ctx.textAlign = 'center';
  ctx.fillText('mm/h', 0, 0);
  ctx.restore();

  // Vẽ các bar
  history.forEach((data, index) => {
    const x = padding.left + index * (chartWidth / 24) + 1;
    const barHeight = (data.value / gridMax) * chartHeight;
    const y = padding.top + chartHeight - barHeight;

    // Màu bar theo mức độ
    const level = getPrecipitationLevel(data.value);
    ctx.fillStyle = level.color;

    // Vẽ bar
    ctx.fillRect(x, y, barWidth, barHeight);
  });

  // Vẽ labels trục X (giờ)
  ctx.fillStyle = '#888';
  ctx.textAlign = 'center';
  ctx.font = '9px sans-serif';

  // Chỉ hiển thị một số giờ để tránh chồng chéo
  const showHours = [1, 4, 7, 10, 13, 16, 19, 22];
  history.forEach((data, index) => {
    if (showHours.includes(data.hour) || index === 23) {
      const x = padding.left + index * (chartWidth / 24) + barWidth / 2;
      const label = `${String(data.hour).padStart(2, '0')}:00`;
      ctx.fillText(label, x, height - 10);
    }
  });
};
```

### 5.4 Component React cho Chart Panel

```jsx
const PrecipitationChartPanel = ({ station, onClose }) => {
  const canvasRef = useRef(null);
  const [history, setHistory] = useState([]);

  useEffect(() => {
    // Fetch 24h history
    fetch24HourHistory(station.id).then(setHistory);
  }, [station.id]);

  useEffect(() => {
    if (canvasRef.current && history.length > 0) {
      drawPrecipitationChart(canvasRef.current, history);
    }
  }, [history]);

  const precip = station.precipitation || {};
  const level = station.level || getPrecipitationLevel(precip.mm1h);

  return (
    <div className="chart-panel">
      {/* Header */}
      <div className="header">
        <h3>☔ {station.name}</h3>
        <span>{station.nameEn} • AMeDAS観測所</span>
        <button onClick={onClose}>×</button>
      </div>

      {/* Current Values */}
      <div className="current-section">
        <div className="main-value">
          <label>現在の降水量</label>
          <span className="value">{precip.mm1h || 0}</span>
          <span className="unit">mm/h</span>
        </div>
        <div className="level-badge" style={{ background: level.color }}>
          {level.label}
        </div>
        <div className="timestamp">
          {new Date(station.timestamp).toLocaleTimeString('ja-JP', {
            hour: '2-digit',
            minute: '2-digit'
          })} 更新
        </div>
      </div>

      {/* Accumulated Values */}
      <div className="accumulated">
        <div className="box">
          <label>3時間</label>
          <span>{precip.mm3h || 0} mm</span>
        </div>
        <div className="box">
          <label>24時間</label>
          <span>{precip.mm24h || 0} mm</span>
        </div>
      </div>

      {/* 24h Chart */}
      <div className="chart-section">
        <label>過去24時間の降水量変化</label>
        <canvas ref={canvasRef} width={320} height={150} />
      </div>

      {/* Color Legend */}
      <div className="legend">
        <label>降水量レベル</label>
        <div className="color-bar">
          {PRECIPITATION_LEVELS.map(l => (
            <div
              key={l.level}
              style={{ background: l.color, flex: 1 }}
              title={l.label}
            />
          ))}
        </div>
        <div className="labels">
          <span>なし</span>
          <span>弱</span>
          <span>中</span>
          <span>強</span>
          <span>猛烈</span>
        </div>
        <div className="level-info">
          <span>🟡 10-20 やや強い</span>
          <span>🟠 20-30 強い</span>
          <span>🔴 30-50 激しい</span>
        </div>
      </div>

      {/* Station Info */}
      <div className="station-info">
        <span>観測所コード: {station.id}</span>
        <span>{station.lat?.toFixed(4)}°N, {station.lon?.toFixed(4)}°E</span>
      </div>
    </div>
  );
};
```

### 5.5 CSS Styles (tham khảo)

```css
.chart-panel {
  position: absolute;
  bottom: 20px;
  right: 20px;
  background: rgba(38, 38, 38, 0.95);
  border-radius: 8px;
  padding: 15px;
  color: white;
  font-family: sans-serif;
  min-width: 340px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.4);
}

.current-section {
  background: rgba(0,0,0,0.3);
  border-radius: 6px;
  padding: 15px;
  margin-bottom: 12px;
}

.main-value .unit {
  font-size: 10px;
  color: #888;
}

.level-badge {
  display: inline-block;
  padding: 6px 16px;
  border-radius: 20px;
  font-weight: bold;
}

.accumulated {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
}

.accumulated .box {
  flex: 1;
  background: rgba(0,0,0,0.2);
  border-radius: 6px;
  padding: 12px;
  text-align: center;
}

.accumulated .box span {
  font-size: 24px;
  font-weight: bold;
}

.chart-section {
  background: rgba(0,0,0,0.3);
  border-radius: 6px;
  padding: 10px;
  margin-bottom: 12px;
}

.color-bar {
  display: flex;
  height: 8px;
  border-radius: 4px;
  overflow: hidden;
}
```


## 7. Tài liệu tham khảo

- [JMA AMeDAS Official](https://www.jma.go.jp/bosai/amedas/)
- [AMeDAS System Overview](https://www.jma.go.jp/jma/kishou/know/amedas/kaisetsu.html)
- [AMeDAS System Overview](https://www.jma.go.jp/jma/kishou/know/amedas/kaisetsu.html)
- [AMeDAS System Overview](https://www.jma.go.jp/jma/kishou/know/amedas/kaisetsu.html)
- [AMeDAS System Overview](https://www.jma.go.jp/jma/kishou/know/amedas/kaisetsu.html)
- [AMeDAS System Overview](https://www.jma.go.jp/jma/kishou/know/amedas/kaisetsu.html)

- commit 3
