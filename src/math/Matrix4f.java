package math;

public class Matrix4f {
	
	private float[][] m;
	
	public Matrix4f() {
		m = new float[4][4];
	}
	
	public Matrix4f initIdentity() {
		m[0][0] = 1;					m[0][1] = 0;				m[0][2] = 0;				m[0][3] = 0;
		m[1][0] = 0;					m[1][1] = 1;				m[1][2] = 0;				m[1][3] = 0;
		m[2][0] = 0;					m[2][1] = 0;				m[2][2] = 1;				m[2][3] = 0;
		m[3][0] = 0;					m[3][1] = 0;				m[3][2] = 0;				m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initPerspective(float fov, float width, float height, float zNear, float zFar) {
		return initPerspective(fov, width / height, zNear, zFar);
	}
	
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float tanHalfFOV = (float)Math.tan(fov / 2);
		float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	m[0][1] = 0;					m[0][2] = 0;						m[0][3] = 0;
		m[1][0] = 0;									m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;						m[1][3] = 0;
		m[2][0] = 0;									m[2][1] = 0;					m[2][2] = (-zNear -zFar)/zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;									m[3][1] = 0;					m[3][2] = 1;						m[3][3] = 0;
		
		return this;
	}
	
	public Matrix4f initRotation(float x, float y, float z) {
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();
		
		rz.m[0][0] = (float)Math.cos(z);rz.m[0][1] = -(float)Math.sin(z);rz.m[0][2] = 0;				rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(z);rz.m[1][1] = (float)Math.cos(z);rz.m[1][2] = 0;					rz.m[1][3] = 0;
		rz.m[2][0] = 0;					rz.m[2][1] = 0;					rz.m[2][2] = 1;					rz.m[2][3] = 0;
		rz.m[3][0] = 0;					rz.m[3][1] = 0;					rz.m[3][2] = 0;					rz.m[3][3] = 1;
		
		rx.m[0][0] = 1;					rx.m[0][1] = 0;					rx.m[0][2] = 0;					rx.m[0][3] = 0;
		rx.m[1][0] = 0;					rx.m[1][1] = (float)Math.cos(x);rx.m[1][2] = -(float)Math.sin(x);rx.m[1][3] = 0;
		rx.m[2][0] = 0;					rx.m[2][1] = (float)Math.sin(x);rx.m[2][2] = (float)Math.cos(x);rx.m[2][3] = 0;
		rx.m[3][0] = 0;					rx.m[3][1] = 0;					rx.m[3][2] = 0;					rx.m[3][3] = 1;
		
		ry.m[0][0] = (float)Math.cos(y);ry.m[0][1] = 0;					ry.m[0][2] = -(float)Math.sin(y);ry.m[0][3] = 0;
		ry.m[1][0] = 0;					ry.m[1][1] = 1;					ry.m[1][2] = 0;					ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(y);ry.m[2][1] = 0;					ry.m[2][2] = (float)Math.cos(y);ry.m[2][3] = 0;
		ry.m[3][0] = 0;					ry.m[3][1] = 0;					ry.m[3][2] = 0;					ry.m[3][3] = 1;
		
		m = rz.mul(ry.mul(rx)).getM();
		
		return this;
	}
	
	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalized();
		
		Vector3f r = up.normalized();
		r = r.cross(f);
		
		Vector3f u = f.cross(r);

		return initRotation(f, u, r);
	}
	
	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right) {
		Vector3f f = forward;
		Vector3f r = right;
		Vector3f u = up;
		
		m[0][0] = r.getX();	m[0][1] = r.getY();	m[0][2] = r.getZ();	m[0][3] = 0;
		m[1][0] = u.getX();	m[1][1] = u.getY();	m[1][2] = u.getZ();	m[1][3] = 0;
		m[2][0] = f.getX();	m[2][1] = f.getY();	m[2][2] = f.getZ();	m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}
	
	public Matrix4f initScale(float scale) {
		return initScale(scale, scale, scale);
	}
	
	public Matrix4f initScale(float x, float y, float z) {
		m[0][0] = x;					m[0][1] = 0;				m[0][2] = 0;				m[0][3] = 0;
		m[1][0] = 0;					m[1][1] = y;				m[1][2] = 0;				m[1][3] = 0;
		m[2][0] = 0;					m[2][1] = 0;				m[2][2] = z;				m[2][3] = 0;
		m[3][0] = 0;					m[3][1] = 0;				m[3][2] = 0;				m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initTranslation(Vector3f t) {
		return initTranslation(t.x, t.y, t.z);
	}
	
	public Matrix4f initTranslation(float x, float y, float z) {
		m[0][0] = 1;					m[0][1] = 0;				m[0][2] = 0;				m[0][3] = x;
		m[1][0] = 0;					m[1][1] = 1;				m[1][2] = 0;				m[1][3] = y;
		m[2][0] = 0;					m[2][1] = 0;				m[2][2] = 1;				m[2][3] = z;
		m[3][0] = 0;					m[3][1] = 0;				m[3][2] = 0;				m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f add(Matrix4f r) {
		Matrix4f res = new Matrix4f();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				res.m[y][x] = m[y][x] + r.m[y][x];
			}
		}
		
		return res;
	}
	
	public Matrix4f inverted() {
		float[] m = new float[4 * 4];
		float[] inv = new float[4 * 4];
		float det;
		int y;
		for (int x = 0; x < 4; x++)
			for (y = 0; y < 4; y++)
				m[x + y * 4] = this.m[y][x];
		
		inv[0] = m[5]  * m[10] * m[15] - 
	             m[5]  * m[11] * m[14] - 
	             m[9]  * m[6]  * m[15] + 
	             m[9]  * m[7]  * m[14] +
	             m[13] * m[6]  * m[11] - 
	             m[13] * m[7]  * m[10];

	    inv[4] = -m[4]  * m[10] * m[15] + 
	              m[4]  * m[11] * m[14] + 
	              m[8]  * m[6]  * m[15] - 
	              m[8]  * m[7]  * m[14] - 
	              m[12] * m[6]  * m[11] + 
	              m[12] * m[7]  * m[10];

	    inv[8] = m[4]  * m[9] * m[15] - 
	             m[4]  * m[11] * m[13] - 
	             m[8]  * m[5] * m[15] + 
	             m[8]  * m[7] * m[13] + 
	             m[12] * m[5] * m[11] - 
	             m[12] * m[7] * m[9];

	    inv[12] = -m[4]  * m[9] * m[14] + 
	               m[4]  * m[10] * m[13] +
	               m[8]  * m[5] * m[14] - 
	               m[8]  * m[6] * m[13] - 
	               m[12] * m[5] * m[10] + 
	               m[12] * m[6] * m[9];

	    inv[1] = -m[1]  * m[10] * m[15] + 
	              m[1]  * m[11] * m[14] + 
	              m[9]  * m[2] * m[15] - 
	              m[9]  * m[3] * m[14] - 
	              m[13] * m[2] * m[11] + 
	              m[13] * m[3] * m[10];

	    inv[5] = m[0]  * m[10] * m[15] - 
	             m[0]  * m[11] * m[14] - 
	             m[8]  * m[2] * m[15] + 
	             m[8]  * m[3] * m[14] + 
	             m[12] * m[2] * m[11] - 
	             m[12] * m[3] * m[10];

	    inv[9] = -m[0]  * m[9] * m[15] + 
	              m[0]  * m[11] * m[13] + 
	              m[8]  * m[1] * m[15] - 
	              m[8]  * m[3] * m[13] - 
	              m[12] * m[1] * m[11] + 
	              m[12] * m[3] * m[9];

	    inv[13] = m[0]  * m[9] * m[14] - 
	              m[0]  * m[10] * m[13] - 
	              m[8]  * m[1] * m[14] + 
	              m[8]  * m[2] * m[13] + 
	              m[12] * m[1] * m[10] - 
	              m[12] * m[2] * m[9];

	    inv[2] = m[1]  * m[6] * m[15] - 
	             m[1]  * m[7] * m[14] - 
	             m[5]  * m[2] * m[15] + 
	             m[5]  * m[3] * m[14] + 
	             m[13] * m[2] * m[7] - 
	             m[13] * m[3] * m[6];

	    inv[6] = -m[0]  * m[6] * m[15] + 
	              m[0]  * m[7] * m[14] + 
	              m[4]  * m[2] * m[15] - 
	              m[4]  * m[3] * m[14] - 
	              m[12] * m[2] * m[7] + 
	              m[12] * m[3] * m[6];

	    inv[10] = m[0]  * m[5] * m[15] - 
	              m[0]  * m[7] * m[13] - 
	              m[4]  * m[1] * m[15] + 
	              m[4]  * m[3] * m[13] + 
	              m[12] * m[1] * m[7] - 
	              m[12] * m[3] * m[5];

	    inv[14] = -m[0]  * m[5] * m[14] + 
	               m[0]  * m[6] * m[13] + 
	               m[4]  * m[1] * m[14] - 
	               m[4]  * m[2] * m[13] - 
	               m[12] * m[1] * m[6] + 
	               m[12] * m[2] * m[5];

	    inv[3] = -m[1] * m[6] * m[11] + 
	              m[1] * m[7] * m[10] + 
	              m[5] * m[2] * m[11] - 
	              m[5] * m[3] * m[10] - 
	              m[9] * m[2] * m[7] + 
	              m[9] * m[3] * m[6];

	    inv[7] = m[0] * m[6] * m[11] - 
	             m[0] * m[7] * m[10] - 
	             m[4] * m[2] * m[11] + 
	             m[4] * m[3] * m[10] + 
	             m[8] * m[2] * m[7] - 
	             m[8] * m[3] * m[6];

	    inv[11] = -m[0] * m[5] * m[11] + 
	               m[0] * m[7] * m[9] + 
	               m[4] * m[1] * m[11] - 
	               m[4] * m[3] * m[9] - 
	               m[8] * m[1] * m[7] + 
	               m[8] * m[3] * m[5];

	    inv[15] = m[0] * m[5] * m[10] - 
	              m[0] * m[6] * m[9] - 
	              m[4] * m[1] * m[10] + 
	              m[4] * m[2] * m[9] + 
	              m[8] * m[1] * m[6] - 
	              m[8] * m[2] * m[5];
		
	    det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];
	    
	    if (det == 0.0f) return new Matrix4f().initIdentity();
	    
	    det = 1.0f / det;
	    
		Matrix4f r = new Matrix4f();
		

		for (int x = 0; x < 4; x++)
			for (y = 0; y < 4; y++)
				r.m[y][x] = m[x + y * 4] * det;
		
		return r;
	}
	
	public Matrix4f mul(float r) {
		Matrix4f res = new Matrix4f();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				res.m[y][x] = r * m[y][x];
			}
		}
		
		return res;
	}
	
	public Matrix4f mul(Matrix4f r) {
		Matrix4f res = new Matrix4f();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				res.m[y][x] = m[y][0] * r.m[0][x] +
						  	  m[y][1] * r.m[1][x] +
						  	  m[y][2] * r.m[2][x] +
						  	  m[y][3] * r.m[3][x];
			}
		}
		
		return res;
	}
	
	public Matrix4f negative() {
		Matrix4f res = new Matrix4f();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				res.m[y][x] = - m[y][x];
			}
		}
		
		return res;
	}
	
	public Matrix4f sub(Matrix4f r) {
		Matrix4f res = new Matrix4f();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				res.m[y][x] = m[y][x] - r.m[y][x];
			}
		}
		
		return res;
	}
	
	public Matrix4f transposed() {
		Matrix4f res = new Matrix4f();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				res.m[y][x] = m[x][y];
			}
		}
		
		return res;
	}
	
	public float get(int x, int y) {
		return m[y][x];
	}
	
	public void set(int x, int y, float value) {
		m[y][x] = value;
	}
	
	public float[][] getM() {
		float[][] res = new float[4][4];
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				res[y][x] = m[y][x];
			}
		}
		
		return res;
	}
	
	@Override
	public String toString() {
		String res = "";
		
		res += "[" + m[0][0] + "\t" + m[0][1] + "\t" + m[0][2] + "\t" + m[0][3] + "\t]\n";
		res += "[" + m[1][0] + "\t" + m[1][1] + "\t" + m[1][2] + "\t" + m[1][3] + "\t]\n";
		res += "[" + m[2][0] + "\t" + m[2][1] + "\t" + m[2][2] + "\t" + m[2][3] + "\t]\n";
		res += "[" + m[3][0] + "\t" + m[3][1] + "\t" + m[3][2] + "\t" + m[3][3] + "\t]";
		
		return res;
	}
	
}
